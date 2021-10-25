package ZMetalHeartY.demo.Controller.WeChat;

import ZMetalHeartY.demo.Entity.FrontUser;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Service.FrontUserService;
import ZMetalHeartY.demo.Service.LoginService;
import ZMetalHeartY.demo.Utils.LoginUtils;
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
@RequestMapping("/WeChat/frontUser")
@ResponseBody
public class WeChatFrontUserController {


    @Autowired
    FrontUserService frontUserService;

    @Autowired
    LoginService loginService;

    @RequestMapping("/login")
    public String weChatLogin(String jsCode){

        return loginService.weChatLogin(jsCode);
    }

    @RequestMapping("/userUpload")
    public String userUpload(FrontUser frontUser) {
        return loginService.WeChatUpdateUserInfo(frontUser);
    }


/*    @RequestMapping("/updatePhone")
    public String updatePhone(FrontUser frontUser){
        org.json.JSONObject json = new org.json.JSONObject();

        if (loginService.updatePhone(weChatUser) > 0){
            json.put("status","succ");
        }else {
            json.put("status","error");
        }

        return json.toString();
    }*/

    @RequestMapping("/getUserInfo")
    public String getUserInfo(@RequestParam("fuId") Integer fuId,@RequestParam("nFuId") Integer nFuId){
        return loginService.getUserByFuId(fuId,nFuId);
    }

    @RequestMapping("/getPhone")
    public String getPhone(String keyStr, String ivStr, String encDataStr){
        LoginUtils loginUtils = new LoginUtils();
        String str = null;
        try {
            str = loginUtils.decrypt(keyStr,ivStr,encDataStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
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
        frontUser.setFuStatus(1);

        ret = frontUserService.insert(frontUser);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }
}
