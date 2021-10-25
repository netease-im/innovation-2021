package ZMetalHeartY.demo.Controller.WeChat;

import ZMetalHeartY.demo.Entity.FrontUserFollow;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Service.FrontUserFollowService;
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
import java.util.List;

@Controller
@RequestMapping("/WeChat/frontUserFollow")
@ResponseBody
public class WeChatFrontUserFollowController {
    
    @Autowired
    FrontUserFollowService frontUserFollowService;

    @RequestMapping("/list")
    public String userlist(){
        return "FrontUserFollow/frontUserFollowList";
    }

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(FrontUserFollow frontUserFollow, Integer page, Integer limit){
        JSONObject json = new JSONObject();
        if(frontUserFollow.getFufFuId() == null && frontUserFollow.getFuId() == null){
            json.put("msg","fuId和fufFuId必须给一个");
            json.put("code","400");
            return json;
        }

        PageInfo<FrontUserFollow> frontUserFollowPageInfo = frontUserFollowService.getFrontUserFollowByPage(frontUserFollow,page,limit);

        json.put("msg","");
        json.put("code","0");
        json.put("data",frontUserFollowPageInfo.getList());
        json.put("count",frontUserFollowPageInfo.getTotal());

        return json;
    }

    @RequestMapping("/getAll")
    @ResponseBody
    public JSONObject getAll(FrontUserFollow frontUserFollow){

        JSONObject json = new JSONObject();

        if(frontUserFollow.getFufFuId() == null && frontUserFollow.getFuId() == null){
            json.put("msg","fuId和fufFuId必须给一个");
            json.put("code","400");
            json.put("data","");
            return json;
        }

        List<FrontUserFollow> frontUserFollows = frontUserFollowService.getAllFrontUserFollow(frontUserFollow);

        json.put("msg","");
        json.put("code","0");
        json.put("data",frontUserFollows);


        return json;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(@RequestParam("fuId") Integer fuId, @RequestParam("fufFuId") Integer fufFuId){
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = frontUserFollowService.delete(fuId,fufFuId);
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
    public String echodata(@RequestParam("fufId") Integer fufId, Model model){
        FrontUserFollow frontUserFollow = frontUserFollowService.selectByFufId(fufId);
        model.addAttribute("frontUserFollow",frontUserFollow);
        return "FrontUserFollow/frontUserFollowEdit";
    }

    @RequestMapping("/addpage")
    public String addpage(FrontUserFollow frontUserFollow, Integer id, Model model) {
        return "FrontUserFollow/frontUserFollowAdd";
    }

    @RequestMapping("/details")
    public String detail(@RequestParam("fufId") Integer fufId, Model model) {

        FrontUserFollow frontUserFollow = frontUserFollowService.selectByFufId(fufId);
        model.addAttribute("frontUserFollow",frontUserFollow);
        return "FrontUserFollow/frontUserFollowDetail";
    }

    @RequestMapping("/getDetails")
    public String getDetails(@RequestParam("fufId") Integer fufId, Model model) {

        JSONObject json = new JSONObject();
        FrontUserFollow frontUserFollow = frontUserFollowService.selectByFufId(fufId);

        if(frontUserFollow != null){
            json.put("msg","");
            json.put("code","0");
            json.put("data",frontUserFollow);
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
    public JSONObject update(FrontUserFollow frontUserFollow){
        System.out.println(JSON.toJSONString(frontUserFollow));
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = frontUserFollowService.updateByFufIdSelective(frontUserFollow);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }


    @RequestMapping("/add")
    @ResponseBody
    public JSONObject insert(FrontUserFollow frontUserFollow){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();

        ret = frontUserFollowService.insert(frontUserFollow);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }
    
}
