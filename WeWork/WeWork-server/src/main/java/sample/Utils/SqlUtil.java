package sample.Utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlUtil {
    public static List<Object> select(Connection c, String sql, Class<?>bean, Object... args){
        PreparedStatement statement;
        List<Object>list=new ArrayList<>();
        try {
            statement=c.prepareStatement(sql);
            for(int i=0;i<args.length;i++) {
                statement.setObject(i+1, args[i]);
            }
            ResultSet set=statement.executeQuery();
            ResultSetMetaData meta=statement.getMetaData();
            while (set.next()){
                Object instance=bean.getDeclaredConstructor().newInstance();
                for(int i=0;i< meta.getColumnCount();i++) {
                    try {
                        Field f = bean.getDeclaredField(meta.getColumnName(i+1));
                        f.setAccessible(true);
                        f.set(instance,set.getObject(i+1));
                    }catch (NoSuchFieldException ignored){
                        // ignore the exception when the field is not found
                    }
                }
                list.add(instance);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                c.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return list;
    }

    public static int executeUpdate(Connection c,String sql,Object... args){
        if(c==null){
            return -1;
        }
        PreparedStatement p;
        int res=-1;
        try {
            p=c.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                p.setObject(i+1,args[i]);
            }
            res=p.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                c.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return res;
    }
}
