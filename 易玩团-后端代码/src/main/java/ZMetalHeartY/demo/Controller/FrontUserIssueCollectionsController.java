package ZMetalHeartY.demo.Controller;

import ZMetalHeartY.demo.Entity.FrontUserIssueCollections;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Service.FrontUserIssueCollectionsService;
import ZMetalHeartY.demo.Service.FrontUserIssueCollectionsService;
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
@RequestMapping("/frontUserIssueCollections")
public class FrontUserIssueCollectionsController {
    @Autowired
    FrontUserIssueCollectionsService frontUserIssueCollectionsService;

    @RequestMapping("/list")
    public String list(){
        return "FrontUserIssueCollections/frontUserIssueCollectionsList";
    }

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(FrontUserIssueCollections frontUserIssueCollections, Integer page, Integer limit){
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session=request.getSession();
        User Account=(User) session.getAttribute("user");

        PageInfo<FrontUserIssueCollections> frontUserIssueCollectionsPageInfo = frontUserIssueCollectionsService.getFrontUserIssueCollectionsByPage(frontUserIssueCollections,page,limit);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",frontUserIssueCollectionsPageInfo.getList());
        json.put("count",frontUserIssueCollectionsPageInfo.getTotal());
        return json;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(@RequestParam("fuId") Integer fuId,@RequestParam("iId") Integer iId){
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = frontUserIssueCollectionsService.deleteByFuicId(fuId,iId);
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
    public String echodata(@RequestParam("fuicId") Integer fuicId, Model model){
        FrontUserIssueCollections frontUserIssueCollections = frontUserIssueCollectionsService.selectByFuicId(fuicId);
        model.addAttribute("frontUserIssueCollections",frontUserIssueCollections);
        return "FrontUserIssueCollections/frontUserIssueCollectionsEdit";
    }

    @RequestMapping("/addpage")
    public String addpage(FrontUserIssueCollections frontUserIssueCollections, Integer id, Model model) {
        return "FrontUserIssueCollections/frontUserIssueCollectionsAdd";
    }

    @RequestMapping("/details")
    public String detail(@RequestParam("fuicId") Integer fuicId, Model model) {

        FrontUserIssueCollections frontUserIssueCollections = frontUserIssueCollectionsService.selectByFuicId(fuicId);
        model.addAttribute("frontUserIssueCollections",frontUserIssueCollections);
        return "FrontUserIssueCollections/frontUserIssueCollectionsDetail";
    }

    @RequestMapping("/getDetails")
    public String getDetails(@RequestParam("fuicId") Integer fuicId, Model model) {

        JSONObject json = new JSONObject();
        FrontUserIssueCollections frontUserIssueCollections = frontUserIssueCollectionsService.selectByFuicId(fuicId);

        if(frontUserIssueCollections != null){
            json.put("msg","");
            json.put("code","0");
            json.put("data",frontUserIssueCollections);
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
    public JSONObject update(FrontUserIssueCollections frontUserIssueCollections){
        System.out.println(JSON.toJSONString(frontUserIssueCollections));
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = frontUserIssueCollectionsService.updateByFuicIdSelective(frontUserIssueCollections);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }


    @RequestMapping("/add")
    @ResponseBody
    public JSONObject insert(FrontUserIssueCollections frontUserIssueCollections){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();

        ret = frontUserIssueCollectionsService.insert(frontUserIssueCollections);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }
    
}
