package ZMetalHeartY.demo.Controller;

import ZMetalHeartY.demo.Entity.FrontUserFollow;
import ZMetalHeartY.demo.Entity.Issue;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Service.FrontUserFollowService;
import ZMetalHeartY.demo.Service.IssueService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/issue")
@Slf4j
public class IssueController {

    @Autowired
    IssueService issueService;

    @RequestMapping("/list")
    public String userlist(){
        return "Issue/issueList";
    }

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(Issue issue, Integer page, Integer limit){
        log.info(JSON.toJSONString(issue));
        PageInfo<Issue> issuePageInfo = issueService.getIssueByPage(issue,page,limit);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",issuePageInfo.getList());
        json.put("count",issuePageInfo.getTotal());
        return json;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(Integer iId){
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = issueService.deleteByIId(iId);
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
    public String echodata(@RequestParam("iId") Integer iId, Model model){
        Issue issue = issueService.selectByIId(iId);
        model.addAttribute("issue",issue);
        return "Issue/issueEdit";
    }

    @RequestMapping("/addpage")
    public String addpage(Issue issue, Integer id, Model model) {
        return "Issue/issueAdd";
    }

    @RequestMapping("/details")
    public String detail(@RequestParam("iId") Integer iId, Model model) {

        Issue issue = issueService.selectByIId(iId);
        model.addAttribute("issue",issue);
        return "Issue/issueDetail";
    }

    @RequestMapping("/getDetails")
    public String getDetails(@RequestParam("iId") Integer iId, Model model) {

        JSONObject json = new JSONObject();
        Issue issue = issueService.selectByIId(iId);

        if(issue != null){
            json.put("msg","");
            json.put("code","0");
            json.put("data",issue);
            json.put("count","");
        }else{
            json.put("msg","");
            json.put("code","");
            json.put("data","");
            json.put("count","");
        }
        return json.toJSONString();
    }

    @RequestMapping("/adopt")
    @ResponseBody
    public String adopt(Integer iId) {
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = issueService.adopt(iId);
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
        return json.toJSONString();
    }

    @RequestMapping("/refuse")
    @ResponseBody
    public String refuse(Integer iId) {
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = issueService.refuse(iId);
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
        return json.toJSONString();
    }

    @RequestMapping("/update")//编辑
    @ResponseBody
    public JSONObject update(Issue issue){
        System.out.println(JSON.toJSONString(issue));
        if(issue.getiStatus()==null){
            issue.setiStatus(1);
        }
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = issueService.updateByIIdSelective(issue);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }


    @RequestMapping("/add")
    @ResponseBody
    public JSONObject insert(Issue issue){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();

        ret = issueService.insert(issue);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }

}
