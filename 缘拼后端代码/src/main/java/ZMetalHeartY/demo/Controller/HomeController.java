package ZMetalHeartY.demo.Controller;


import ZMetalHeartY.demo.Entity.*;
import ZMetalHeartY.demo.Mapper.UserMapper;
import ZMetalHeartY.demo.Service.FlatformAdminGroupService;
import ZMetalHeartY.demo.Service.FlatformAdminMenuService;
import ZMetalHeartY.demo.Service.UserService;
import ZMetalHeartY.demo.Utils.MakeMenuTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;


/**
 * 主页和登录页面映射
 */
@Controller
public class HomeController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private FlatformAdminGroupService flatformAdminGroupService;
    @Autowired
    private FlatformAdminMenuService flatformAdminMenuService;
    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(Authentication authentication, Model model, HttpSession session, HttpServletRequest request){

        User user = userMapper.getByUsername(authentication.getName());

        FlatformAdminGroup group_info = flatformAdminGroupService.findById(user.getAg_id());
        MakeMenuTree menuTree = new MakeMenuTree();
        List<FlatformAdminMenu> menu_list = flatformAdminMenuService.findAll();
        menu_list=menuTree.menuList(menu_list);
        List<FlatformAdminMenu> auth_list = menuTree.makeAuthList(menu_list,group_info);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Collection<GrantedAuthority> authorityCollection = (Collection<GrantedAuthority>) auth.getAuthorities();
            model.addAttribute("authorities", authorityCollection.toString());
            System.out.println(authorityCollection.toString()+"----------------------------------------------");
        }


        session.setAttribute("SESSIONAUTHLIST",auth_list);
        session.setAttribute("user",user);
        return "index";
    }



    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }



    @RequestMapping("/shouye")
    public String shouye(Model model) {
        return "shouye";
    }

    @RequestMapping("/csrf")
    @ResponseBody
    public String csrfz(Model model) {
        return null;
    }






}
