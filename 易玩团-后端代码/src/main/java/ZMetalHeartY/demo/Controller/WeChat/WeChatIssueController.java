package ZMetalHeartY.demo.Controller.WeChat;

import ZMetalHeartY.demo.Entity.*;
import ZMetalHeartY.demo.Mapper.CurrencyMapper;
import ZMetalHeartY.demo.Mapper.FrontUserMapper;
import ZMetalHeartY.demo.Mapper.IssueAttendMapper;
import ZMetalHeartY.demo.Mapper.IssueCommentMapper;
import ZMetalHeartY.demo.Service.IssueCommentService;
import ZMetalHeartY.demo.Service.IssueService;
import ZMetalHeartY.demo.Utils.DateUtils;
import ZMetalHeartY.demo.Utils.GSUtil;
import ZMetalHeartY.demo.Utils.Graphic;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/WeChat/issue")
@ResponseBody
public class WeChatIssueController {

    @Autowired
    CurrencyMapper currencyMapper;
    @Autowired
    IssueService issueService;
    @Autowired
    IssueCommentMapper issueCommentMapper;
    @Autowired
    FrontUserMapper frontUserMapper;
    @Autowired
    IssueAttendMapper issueAttendMapper;
    @Autowired
    Graphic graphic;

    @RequestMapping("/list")
    public String userlist(){
        return "Issue/issueList";
    }

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(Issue issue,@RequestParam("fromFuId") Integer fromFuId, Integer page, Integer limit){

        PageInfo<Issue> issuePageInfo = issueService.getIssueByPage(issue,page,limit);

        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",issueService.getGetListContent(issuePageInfo.getList(),fromFuId));
        json.put("total",issuePageInfo.getTotal());
        return json;
    }

    @RequestMapping("/getByIIdList")
    @ResponseBody
    public JSONObject getByIIdList(@RequestParam("iIds") List<Integer> iIds,Integer fromFuId){

        JSONObject json = new JSONObject();
        System.out.println(JSON.toJSONString(iIds));
        System.out.println(fromFuId);
        List<Issue> issues = new ArrayList<>();
        for (Integer iId:iIds) {
            issues.add(issueService.selectByIId(iId));
        }

        json.put("msg","");
        json.put("code","0");
        json.put("data",issueService.getGetListContent(issues,fromFuId));
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
    public String getDetails(@RequestParam("iId") Integer iId,@RequestParam("fromFuId") Integer fromFuId, Model model) {

        JSONObject json = new JSONObject();
        Issue issue = issueService.selectByIId(iId);

        issueService.getGetIssueContent(issue,fromFuId);

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
        issue.setiCreateTime(new Date());
        ret = issueService.insert(issue);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }

    @RequestMapping(value = "/getIssueByKilometers")
    public String getIssueByKilometers(Issue issue ,@RequestParam("fromFuId") Integer fromFuId, Double longitude , Double latitude) {

        JSONObject json = new JSONObject();
        issue.setiStatus(1);
        if(issue.getiCreateTime() == null){
            issue.setiCreateTime(new Date());
        }

        Kilometers kilometers = currencyMapper.getKilometersByName("max_display");
        double k = kilometers.getKilometers();


        List<Issue> finalissueList = new ArrayList<>();
        List<Issue> issueList = issueService.getAllIssue(issue);
        GSUtil gsUtil = new GSUtil();


        for (Issue is:issueList) {
            JSONObject jsonAddress = JSONObject.parseObject(is.getiAddress());

            JSONObject locationJson = JSON.parseObject( String.valueOf(jsonAddress.get("location")) );
            if (jsonAddress == null || locationJson.get("latitude") == null || locationJson.get("longitude") == null){
                continue;
            }
            //公里数过滤（低效率待改进）
            if (k*1000>=gsUtil.getmeter(longitude,latitude,((BigDecimal) locationJson.get("longitude")).doubleValue(),((BigDecimal)locationJson.get("latitude")).doubleValue()) ){
                finalissueList.add(is);
            }

        }

        JSONArray finaljson = new JSONArray();
        issueService.getGetListContent(finalissueList,fromFuId);

        json.put("Kilometers",k);
        json.put("IssueList",finalissueList);

        return JSON.toJSONString(json);

    }

}
