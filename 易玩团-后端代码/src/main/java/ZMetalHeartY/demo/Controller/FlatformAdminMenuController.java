package ZMetalHeartY.demo.Controller;

import ZMetalHeartY.demo.Entity.FlatformAdminMenu;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Service.FlatformAdminMenuService;
import ZMetalHeartY.demo.Service.UserService;
import ZMetalHeartY.demo.Utils.TimeUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;


@Controller
public class FlatformAdminMenuController {

    @Autowired
    private FlatformAdminMenuService flatformAdminMenuService;
    @Autowired
    private UserService flatformAccountService;

    @RequestMapping("/menu/list")
    public String list(){
        return "menulist";
    }

    @RequestMapping("/menu/listjson")
    @ResponseBody
    public JSONObject listjson(FlatformAdminMenu flatformAdminMenu, Integer page, Integer limit){
        List<FlatformAdminMenu> flatformAccounts = flatformAdminMenuService.findByPage(flatformAdminMenu,page,limit);
        int count = flatformAdminMenuService.count(flatformAdminMenu);

        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",flatformAccounts);//list
        json.put("count",count);//count值

        return json;
    }

    @RequestMapping("/menu/add")
    public String menu(){
        return "menuadd";
    }

    @RequestMapping("/menu/echodata")
    public String echodata(Integer id, Model model){
        FlatformAdminMenu flatformAdminMenu = flatformAdminMenuService.findById(id);
        model.addAttribute("flatformAdminMenu",flatformAdminMenu);
        return "menuedit";
    }

    @RequestMapping("/menu/insert")
    @ResponseBody
    public JSONObject insert(Authentication authentication, FlatformAdminMenu flatformAdminMenu){
        int ret = 0;
        if(flatformAdminMenu.getAu_clickable()==null){
            flatformAdminMenu.setAu_clickable(1);
        }
        if(flatformAdminMenu.getAu_state()==null){
            flatformAdminMenu.setAu_state(1);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        User flatformAccount = flatformAccountService.findByAccount(authentication.getName());
        flatformAdminMenu.setU_id(flatformAccount.getUserid());
        flatformAdminMenu.setCreate_time(TimeUtil.getCurrentTime());
        ret = flatformAdminMenuService.insert(flatformAdminMenu);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        JSONObject json = (JSONObject) JSONObject.toJSON(map);
        return json;
    }

    @RequestMapping("/menu/update")
    @ResponseBody
    public JSONObject update(FlatformAdminMenu flatformAdminMenu, String auclickable){

        if(auclickable==null){
            flatformAdminMenu.setAu_clickable(0);
        }else if (auclickable.equals("on")){
            flatformAdminMenu.setAu_clickable(1);
        }
        if(flatformAdminMenu.getAu_state()==null){
            flatformAdminMenu.setAu_state(1);
        }
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = flatformAdminMenuService.update(flatformAdminMenu);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        JSONObject json = (JSONObject) JSONObject.toJSON(map);
        return json;
    }

    @RequestMapping("/menu/delete")
    @ResponseBody
    public JSONObject delete(FlatformAdminMenu flatformAdminMenu){
        int ret = 0;
        JSONObject json = new JSONObject();
        flatformAdminMenu.setDelete_status(1);
        flatformAdminMenu.setDelete_time(TimeUtil.getCurrentTime());
        ret = flatformAdminMenuService.delete(flatformAdminMenu);
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

    @RequestMapping("/menu/findByPid")
    @ResponseBody
    public JSONObject findByPid(Integer au_p_id){
        JSONObject json = new JSONObject();
        FlatformAdminMenu flatformAdminMenu = flatformAdminMenuService.findByPid(au_p_id);
        if(flatformAdminMenu!=null){
            json.put("flatformAdminMenu",flatformAdminMenu);
            json.put("status","succ");
        }else{
            json.put("status","error");
        }
        return json;
    }

    @RequestMapping("/menu/getMenuList")
    @ResponseBody
    public JSONObject findMenuList(Integer value){
        JSONObject json = new JSONObject();
        List<FlatformAdminMenu> flatformAdminMenus = flatformAdminMenuService.findByMenu(value);
        if(flatformAdminMenus!=null && flatformAdminMenus.size()>0){
            json.put("flatformAdminMenus",flatformAdminMenus);
            json.put("status","succ");
        }else{
            json.put("status","error");
        }
        return json;
    }
}
