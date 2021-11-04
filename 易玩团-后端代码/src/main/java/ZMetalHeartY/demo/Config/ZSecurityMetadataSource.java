package ZMetalHeartY.demo.Config;

import ZMetalHeartY.demo.Entity.FlatformAdminGroup;
import ZMetalHeartY.demo.Entity.FlatformAdminMenu;
import ZMetalHeartY.demo.Mapper.FlatformAdminGroupMapper;
import ZMetalHeartY.demo.Mapper.FlatformAdminMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 自定义的元数据源类，用来提供鉴权过程中，访问资源所需的角色
 */
@Component
public class ZSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    //@Autowired
   // ResourceMapper resourceMapper;
    @Autowired
    FlatformAdminGroupMapper flatformAdminGroupMapper;
    @Autowired
    FlatformAdminMenuMapper flatformAdminMenuMapper;

    //本方法返回访问资源所需的角色集合
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //从object中得到需要访问的资源，即网址
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        //从数据库中得到所有资源，以及对应的角色
        //List<Resource> resourceBeans = resourceMapper.selectAllResource();

        List<FlatformAdminMenu> flatformAdminMenus = flatformAdminMenuMapper.findAll();



        for (FlatformAdminMenu fam : flatformAdminMenus) {
            //首先进行地址匹配
            if (antPathMatcher.match(fam.getAu_url(), requestUrl)) {

                List<String> strings = new ArrayList<>();
                FlatformAdminGroup flatformAdminGroup = new FlatformAdminGroup();
                List<FlatformAdminGroup> flatformAdminGroups=flatformAdminGroupMapper.findAll(flatformAdminGroup);
                for (FlatformAdminGroup fag:flatformAdminGroups){
                    //System.out.println(fag.getRole()+"----------------");
                    String s = fag.getAg_auth();
                    String[] l= s.split(",");
                    List<String> list = Arrays.asList(l);
                    //System.out.println(requestUrl+":"+list.size()+"个"+":"+fam.getAu_id());
                    if (list.contains(fam.getAu_id()+"")){
                        strings.add(fag.getRole()+"");
                    }
                }



                String[] arr = strings.toArray(new String[strings.size()]);

                if (arr.length>0) {
                    return SecurityConfig.createList(arr);
                }
            }
        }


/*
        for (Resource resource : resourceBeans) {
            //首先进行地址匹配
            if (antPathMatcher.match(resource.getUrl(), requestUrl)
                    && resource.getRolesArray().length > 0) {
                return SecurityConfig.createList(resource.getRolesArray());
            }
        }*/

        //匹配不成功返回一个特殊的ROLE_NONE
        return SecurityConfig.createList("ROLE_NONE");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
