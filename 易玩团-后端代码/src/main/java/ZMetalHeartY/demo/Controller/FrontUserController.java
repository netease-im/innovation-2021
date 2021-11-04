package ZMetalHeartY.demo.Controller;

import ZMetalHeartY.demo.Entity.FrontUser;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Service.FrontUserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/frontUser")
public class FrontUserController {


    @Autowired
    FrontUserService frontUserService;

    @RequestMapping("/list")
    public String userlist(){
        return "FrontUser/frontUserList";
    }

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(FrontUser frontUser, Integer page, Integer limit){
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session=request.getSession();
        User Account=(User) session.getAttribute("user");

        PageInfo<FrontUser> frontUserPageInfo = frontUserService.getFrontUserByPage(frontUser,page,limit);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",frontUserPageInfo.getList());
        json.put("count",frontUserPageInfo.getTotal());
        return json;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(Integer fuId){
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = frontUserService.deleteByFuId(fuId);
        if(ret>0){
            json.put("msg","");
            json.put("code","0");
            json.put("data","");
            json.put("count","");
        }else{
            json.put("msg","");
            json.put("code","");
            json.put("data","");
            json.put("count","");
        }
        return json;
    }

    @RequestMapping("/echodata")
    public String echodata(@RequestParam("fuId") Integer fuId, Model model){
        FrontUser frontUser = frontUserService.selectByFuId(fuId);
        model.addAttribute("frontUser",frontUser);
        return "FrontUser/frontUserEdit";
    }

    @RequestMapping("/addpage")
    public String addpage(FrontUser frontUser, Integer id, Model model) {
        return "FrontUser/frontUserAdd";
    }

    @RequestMapping("/details")
    public String detail(@RequestParam("fuId") Integer fuId, Model model) {

        FrontUser frontUser = frontUserService.selectByFuId(fuId);
        model.addAttribute("frontUser",frontUser);
        return "FrontUser/frontUserDetail";
    }

    @RequestMapping("/getDetails")
    public String getDetails(@RequestParam("fuId") Integer fuId, Model model) {

        JSONObject json = new JSONObject();
        FrontUser frontUser = frontUserService.selectByFuId(fuId);

        if(frontUser != null){
            json.put("msg","");
            json.put("code","0");
            json.put("data",frontUser);
            json.put("count","");
        }else{
            json.put("msg","");
            json.put("code","");
            json.put("data","");
            json.put("count","");
        }
        return json.toJSONString();
    }


    @RequestMapping("/update")//编辑
    @ResponseBody
    public JSONObject update(FrontUser frontUser){
        System.out.println(JSON.toJSONString(frontUser));
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = frontUserService.updateByFuIdSelective(frontUser);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }


    @RequestMapping("/add")
    @ResponseBody
    public JSONObject insert(FrontUser frontUser){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();

        ret = frontUserService.insert(frontUser);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }
}
