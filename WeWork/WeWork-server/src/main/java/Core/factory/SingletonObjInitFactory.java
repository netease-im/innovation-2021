package Core.factory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import Core.annotation.Inject;
import Core.annotation.Singleton;
import Core.annotation.SingletonObjHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingletonObjInitFactory {
    private Logger log= LoggerFactory.getLogger(SingletonObjInitFactory.class);
    private static SingletonObjInitFactory Instance;
    Map<Class<?>,Object>factory=new HashMap<>();
    List<String>className=new ArrayList<>();
    String base= this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

    public static SingletonObjInitFactory getInstance() {
        return Instance;
    }

    public static void setInstance(SingletonObjInitFactory instance) {
        Instance = instance;
    }

    public SingletonObjInitFactory(String[]paths) {

        if(base.endsWith(".jar")){
            dealWithJarProject(paths);
        }else{
            dealWithLocalProject(paths);
        }
        for(String x:className){
            try {

                SingletonObjHandler ss=Class.forName(x).getAnnotation(SingletonObjHandler.class);
                if(ss==null){
                    continue;
                }
                Class<?>class_=Class.forName(x);
                Method[]methods=class_.getDeclaredMethods();
                Object o=class_.getDeclaredConstructor().newInstance();

                for(Method m:methods){
                    Singleton s=m.getAnnotation(Singleton.class);
                    if(s!=null){
                        Object instance=getSingleton(m,o);


                        //set fields
                        String propertyPath=s.property();
                        if(!propertyPath.equals("")){
                        InputStream i = this.getClass().getClassLoader().getResourceAsStream(propertyPath);
                        Properties properties=new Properties();
                        properties.load(i);
                        for(Object key:properties.keySet()) {
                            String k = (String) key;
                            try {
                                Field f = instance.getClass().getDeclaredField(k);
                                f.setAccessible(true);
                                f.set(instance, properties.get(key));
                            }catch (NoSuchFieldException ignored){
                                //ignore the exception when the field is not found
                            }
                          }
                        }


                        //save the singleton
                        factory.put(instance.getClass(),instance);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(Class<?> c: factory.keySet()){
            Object instance=factory.get(c);
            //inject field
            Field[]fields=instance.getClass().getDeclaredFields();
            for(Field f:fields){
                Inject inject=f.getAnnotation(Inject.class);
                if(inject!=null){
                    f.setAccessible(true);
                    try {
                        f.set(instance,factory.get(f.getType()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Object getSingleton(Method m, Object o){
        try {
            //o:m所属的实例化对象
            return m.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    public void dealWithLocalProject(String[]paths){
        for(String p:paths){
            String locate=p.replaceAll("\\.","\\/");
            String fullPath=base+locate;
            getAllClassName(new File(fullPath),p,true);
            getAllClassName(new File(fullPath+".class"),p,false);
        }
    }

    public void dealWithJarProject(String[]paths){
        try {
            JarFile jar=new JarFile(new File(base));
            Enumeration<JarEntry>e=jar.entries();
            Set<String>set=new HashSet<>();
            for(String p:paths){
                p=p.replaceAll("\\.","/");
                set.add(p+"/");
                set.add(p);
            }
            while (e.hasMoreElements()){
                JarEntry next=e.nextElement();
                String packageName=next.getName();
                if(set.contains(packageName)){
                    while (e.hasMoreElements()){
                        String match=e.nextElement().getName();
                        String regex="^"+packageName+".*";
                        if(match.matches(regex)){
                            if(match.endsWith(".class")){
                                match=match.substring(0,match.indexOf("."));
                                match=match.replaceAll("/",".");
                                className.add(match);
                            }
                        }else{
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getAllClassName(File f,String context,boolean isDirectory){
        if(!f.exists()){
            return;
        }
        if(!f.isDirectory()){
            addClassName(f,context,isDirectory);
            return;
        }
        File[]ff=f.listFiles();
        if(ff!=null) {
            for (File x : ff) {
                if (x.isDirectory()) {
                    getAllClassName(x,context,isDirectory);
                } else {
                    addClassName(x,context,isDirectory);
                }
            }
        }
    }

    private void addClassName(File f,String context,boolean isDirectory){
                String s=f.getName();
                if(f.exists()) {
                    if(isDirectory) {
                        if (s.endsWith(".class")) {
                            String fullName = context + "." + f.getName().replaceAll("\\.class", "");
                            className.add(fullName);
                        }
                    }else{
                            className.add(context.replaceAll("\\.class",""));
                    }
                }
    }

    public Object getSingletonInstance(Class<?>c){
        return SingletonObjInitFactory.getInstance().factory.get(c);
    }

}
