package ZMetalHeartY.demo.Controller;

import ZMetalHeartY.demo.Entity.FrontUserIssueLike;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Service.FrontUserIssueLikeService;
import ZMetalHeartY.demo.Service.FrontUserIssueLikeService;
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
@RequestMapping("/frontUserIssueLike")
public class FrontUserIssueLikeController {
    @Autowired
    FrontUserIssueLikeService frontUserIssueLikeService;

    @RequestMapping("/list")
    public String list(){
        return "FrontUserIssueLike/frontUserIssueLikeList";
    }

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(FrontUserIssueLike frontUserIssueLike, Integer page, Integer limit){
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session=request.getSession();
        User Account=(User) session.getAttribute("user");

        PageInfo<FrontUserIssueLike> frontUserIssueLikePageInfo = frontUserIssueLikeService.getFrontUserIssueLikeByPage(frontUserIssueLike,page,limit);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",frontUserIssueLikePageInfo.getList());
        json.put("count",frontUserIssueLikePageInfo.getTotal());
        return json;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(@RequestParam("fuId") Integer fuId,@RequestParam("iId") Integer iId){
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = frontUserIssueLikeService.deleteByFuilId(fuId,iId);
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
    public String echodata(@RequestParam("fuilId") Integer fuilId, Model model){
        FrontUserIssueLike frontUserIssueLike = frontUserIssueLikeService.selectByFuilId(fuilId);
        model.addAttribute("frontUserIssueLike",frontUserIssueLike);
        return "FrontUserIssueLike/frontUserIssueLikeEdit";
    }

    @RequestMapping("/addpage")
    public String addpage(FrontUserIssueLike frontUserIssueLike, Integer id, Model model) {
        return "FrontUserIssueLike/frontUserIssueLikeAdd";
    }

    @RequestMapping("/details")
    public String detail(@RequestParam("fuilId") Integer fuilId, Model model) {

        FrontUserIssueLike frontUserIssueLike = frontUserIssueLikeService.selectByFuilId(fuilId);
        model.addAttribute("frontUserIssueLike",frontUserIssueLike);
        return "FrontUserIssueLike/frontUserIssueLikeDetail";
    }

    @RequestMapping("/getDetails")
    public String getDetails(@RequestParam("fuilId") Integer fuilId, Model model) {

        JSONObject json = new JSONObject();
        FrontUserIssueLike frontUserIssueLike = frontUserIssueLikeService.selectByFuilId(fuilId);

        if(frontUserIssueLike != null){
            json.put("msg","");
            json.put("code","0");
            json.put("data",frontUserIssueLike);
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
    public JSONObject update(FrontUserIssueLike frontUserIssueLike){
        System.out.println(JSON.toJSONString(frontUserIssueLike));
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = frontUserIssueLikeService.updateByFuilIdSelective(frontUserIssueLike);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }


    @RequestMapping("/add")
    @ResponseBody
    public JSONObject insert(FrontUserIssueLike frontUserIssueLike){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();

        ret = frontUserIssueLikeService.insert(frontUserIssueLike);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }
}
