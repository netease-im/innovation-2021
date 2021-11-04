package ZMetalHeartY.demo.Controller;

import ZMetalHeartY.demo.Entity.*;
import ZMetalHeartY.demo.Mapper.UserMapper;
import ZMetalHeartY.demo.Service.FlatformAdminGroupService;
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

import java.util.ArrayList;
import java.util.List;


@Controller
public class FlatformAdminGroupController{

    @Autowired
    UserMapper userMapper;
    @Autowired
    private FlatformAdminGroupService flatformAdminGroupService;
    @Autowired
    private FlatformAdminMenuService flatformAdminMenuService;
    @Autowired
    private UserService userService;


    @RequestMapping("/group/list")
    public String grouplist(){
        return "grouplist";
    }

    @RequestMapping("/group/add")
    public String groupadd(){
        return "groupaddaccount";
    }




    @RequestMapping("/group/addagent")
    public String groupagent(){
        return "groupaddagent";
    }

    @RequestMapping("/group/listjson")
    @ResponseBody
    public JSONObject grouplistjson(FlatformAdminGroup flatformAdminGroup, Integer page, Integer limit){

        List<FlatformAdminGroup> flatformAdminGroups = flatformAdminGroupService.findByPage(flatformAdminGroup,page,limit);
        int count = flatformAdminGroupService.count(flatformAdminGroup);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","");
        json.put("data",flatformAdminGroups);//list
        json.put("count",count);//count值
        return json;

    }
    @RequestMapping("/group/insert")//添加
    @ResponseBody
    public JSONObject insert(Authentication authentication, FlatformAdminGroup flatformAdminGroup){
        int ret = 0;
        JSONObject json = new JSONObject();
        User flatformAccount = userService.findByAccount(authentication.getName());

        flatformAdminGroup.setU_id(flatformAccount.getUserid());
        flatformAdminGroup.setCreate_time(TimeUtil.getCurrentTime());
        flatformAdminGroup.setDelete_status(0);
        flatformAdminGroup.setAg_status(1);
        ret = flatformAdminGroupService.insert(flatformAdminGroup);
        if(ret>0){
            json.put("status","succ");
        }else{
            json.put("status","error");
        }
        return json;
    }


    @RequestMapping("/group/echodata")
    public String echodata(Integer id, Model model){
        FlatformAdminGroup flatformAdminGroup  = flatformAdminGroupService.findById(id);
        model.addAttribute("flatformAdminGroup",flatformAdminGroup);
        return "groupedit";
    }

    @RequestMapping("/group/update")//编辑
    @ResponseBody
    public JSONObject update(FlatformAdminGroup flatformAdminGroup){
        JSONObject json = new JSONObject();

        int ret = 0;
        ret = flatformAdminGroupService.update(flatformAdminGroup);
        if(ret>0){
            json.put("status","succ");
        }else{
            json.put("status","error");
        }
        return json;
    }

    @RequestMapping("/group/delete")
    @ResponseBody
    public JSONObject delete(FlatformAdminGroup flatformAdminGroup){

        JSONObject json = new JSONObject();
        int ret = 0;
        flatformAdminGroup.setDelete_status(1);
        flatformAdminGroup.setDelete_time(TimeUtil.getCurrentTime());
        ret = flatformAdminGroupService.delete(flatformAdminGroup);
        if(ret>0){
            json.put("status","succ");
        }else{
            json.put("status","error");
        }
        return json;
    }


    @RequestMapping("/group/testjson")
    @ResponseBody
    public JSONObject testjson(Integer id, String auType){
        JSONObject json = new JSONObject();
        FlatformAdminGroup flatformAdminGroup = flatformAdminGroupService.findById(id);
        String auth = flatformAdminGroup.getAg_auth();
        String[] authlist = auth.split(",");
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        if(authlist!=null&&authlist.length>0)
            for(int i = 0;i<authlist.length;i++){
                arrayList.add(Integer.parseInt(authlist[i]));
            }
        Tree tree = new Tree();
        List<MenuTree> menuTreelist = new ArrayList<>();
        List<FlatformAdminMenu> flatformAdminMenu = flatformAdminMenuService.findByType(auType);
        if(flatformAdminMenu!=null&&flatformAdminMenu.size()>0) {
            for (int i = 0; i < flatformAdminMenu.size(); i++) {
                MenuTree menuTree = new MenuTree();
                menuTree.setId(flatformAdminMenu.get(i).getAu_id());
                menuTree.setName(flatformAdminMenu.get(i).getAu_title());
                menuTree.setPid(flatformAdminMenu.get(i).getAu_p_id());
                menuTreelist.add(menuTree);
            }
        }
        tree.setList(menuTreelist);
        tree.setCheckedId(arrayList);
        json.put("code","0");
        json.put("msg","获取成功");
        json.put("data",tree);
        return json;
    }



    @RequestMapping("/group/treeAll")//权限数
    @ResponseBody
    public JSONObject treeAll(Integer id, String auType){
        JSONObject json = new JSONObject();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Tree tree = new Tree();
        List<MenuTree> menuTreelist = new ArrayList<>();
        List<FlatformAdminMenu> flatformAdminMenu = flatformAdminMenuService.findByType(auType);
        if(flatformAdminMenu!=null&&flatformAdminMenu.size()>0)
            for(int i = 0;i<flatformAdminMenu.size();i++) {
                MenuTree menuTree = new MenuTree();
                menuTree.setId(flatformAdminMenu.get(i).getAu_id());
                menuTree.setName(flatformAdminMenu.get(i).getAu_title());
                menuTree.setPid(flatformAdminMenu.get(i).getAu_p_id());
                menuTreelist.add(menuTree);
            }
        tree.setList(menuTreelist);
        tree.setCheckedId(arrayList);
        json.put("code","0");
        json.put("msg","获取成功");
        json.put("data",tree);
        return json;
    }

}
