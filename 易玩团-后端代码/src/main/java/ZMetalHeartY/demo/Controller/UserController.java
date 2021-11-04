package ZMetalHeartY.demo.Controller;

import ZMetalHeartY.demo.Entity.FlatformAdminGroup;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Mapper.UserMapper;
import ZMetalHeartY.demo.Service.FlatformAdminGroupService;
import ZMetalHeartY.demo.Service.UserService;
import ZMetalHeartY.demo.Utils.GeneratePassword;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private FlatformAdminGroupService flatformAdminGroupService;
    @Autowired
    private UserService userService;



    @RequestMapping("/list")
    public String userlist(User user, Integer currentPage, Integer pageSize, Model model){
        List<User> flatformAccountList = userService.findByPage(user,currentPage,pageSize);
        model.addAttribute("flatformAccountList",flatformAccountList);
        return "list";
    }

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(User user, Integer page, Integer limit){
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session=request.getSession();
        User Account=(User) session.getAttribute("user");
        if(Account.getAg_id()!=2){
            user.setUserid(Account.getUserid());
        }
        List<User> users = userService.findByPage(user,page,limit);
        int count = userMapper.count(user);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",users);//list
        json.put("count",count);//count值
        //System.out.println(json);
        return json;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(Integer userid){
        int ret = 0;
        JSONObject json = new JSONObject();
        User user = new User();
        user.setUserid(userid);
        ret = userMapper.delete(userid);
        if(ret>0){
            json.put("msg","");
            json.put("code","0");
            json.put("data","");//list
            json.put("count","");//count值
        }else{
            json.put("msg","");
            json.put("code","");
            json.put("data","");//list
            json.put("count","");//count值
        }
        return json;
    }

    @RequestMapping("/echodata")
    public String echodata(FlatformAdminGroup flatformAdminGroup, Integer userid, Model model){
        User user = userMapper.getByID(userid);
        List<FlatformAdminGroup> flatformAdminGroups = flatformAdminGroupService.findAll(flatformAdminGroup);
        model.addAttribute("flatformAccount",user);
        model.addAttribute("flatformAdminGroups",flatformAdminGroups);
        return "edit";
    }

    @RequestMapping("/add")
    public String userechodata(FlatformAdminGroup flatformAdminGroup, Integer id, Model model) {
        List<FlatformAdminGroup> flatformAdminGroups = flatformAdminGroupService.findAll(flatformAdminGroup);
        model.addAttribute("flatformAdminGroups", flatformAdminGroups);

        return "add";
    }

    @RequestMapping("/update")//编辑
    @ResponseBody
    public JSONObject update(User user){

        System.out.println(user.getStatus());
        if(user.getStatus()==null){
            user.setStatus(1);
        }
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        user.setPassword(GeneratePassword.generatePassword(user.getPassword()));
        ret = userMapper.update(user);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        JSONObject json = (JSONObject) JSONObject.toJSON(map);
        return json;
    }

    @RequestMapping("/register")
    public String register(FlatformAdminGroup flatformAdminGroup, Integer id, Model model) {
        return "register";
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JSONObject insert(User user){
        System.out.println(user.getAg_id());
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        user.setPassword(GeneratePassword.generatePassword(user.getPassword()));
        ret = userMapper.register(user);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        JSONObject json = (JSONObject) JSONObject.toJSON(map);
        return json;
    }

}
