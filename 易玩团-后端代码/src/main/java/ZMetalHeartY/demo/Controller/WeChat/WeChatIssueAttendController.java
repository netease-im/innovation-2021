package ZMetalHeartY.demo.Controller.WeChat;

import ZMetalHeartY.demo.Entity.IssueAttend;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Service.IssueAttendService;
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
@RequestMapping("/WeChat/issueAttend")
@ResponseBody
public class WeChatIssueAttendController {

    @Autowired
    IssueAttendService issueAttendService;

    @RequestMapping("/list")
    public String userlist(){
        return "IssueAttend/issueAttendList";
    }

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(IssueAttend issueAttend, Integer page, Integer limit){

        PageInfo<IssueAttend> issueAttendPageInfo = issueAttendService.getIssueAttendByPage(issueAttend,page,limit);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",issueAttendPageInfo.getList());
        json.put("count",issueAttendPageInfo.getTotal());
        return json;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(Integer iaId){
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = issueAttendService.deleteByIaId(iaId);
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
    public String echodata(@RequestParam("iaId") Integer iaId, Model model){
        IssueAttend issueAttend = issueAttendService.selectByIaId(iaId);
        model.addAttribute("issueAttend",issueAttend);
        return "IssueAttend/issueAttendEdit";
    }

    @RequestMapping("/addpage")
    public String addpage(IssueAttend issueAttend, Integer id, Model model) {
        return "IssueAttend/issueAttendAdd";
    }

    @RequestMapping("/details")
    public String detail(@RequestParam("iaId") Integer iaId, Model model) {

        IssueAttend issueAttend = issueAttendService.selectByIaId(iaId);
        model.addAttribute("issueAttend",issueAttend);
        return "IssueAttend/issueAttendDetail";
    }

    @RequestMapping("/getDetails")
    public String getDetails(@RequestParam("iaId") Integer iaId, Model model) {

        JSONObject json = new JSONObject();
        IssueAttend issueAttend = issueAttendService.selectByIaId(iaId);

        if(issueAttend != null){
            json.put("msg","");
            json.put("code","0");
            json.put("data",issueAttend);
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
    public JSONObject update(IssueAttend issueAttend){
        System.out.println(JSON.toJSONString(issueAttend));
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = issueAttendService.updateByIaIdSelective(issueAttend);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }

    @RequestMapping("/adopt")//编辑
    @ResponseBody
    public JSONObject adopt(@RequestParam("iaId") Integer iaId){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = issueAttendService.adopt(iaId);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }

    @RequestMapping("/refuse")//编辑
    @ResponseBody
    public JSONObject refuse(@RequestParam("iaId") Integer iaId){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = issueAttendService.refuse(iaId);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }



    @RequestMapping("/add")
    @ResponseBody
    public JSONObject insert(IssueAttend issueAttend){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();

        ret = issueAttendService.insert(issueAttend);
        if(ret == -1){
            map.put("status","error");
            map.put("msg","该拼团已结束");
        }else if(ret == -2){
            map.put("status","error");
            map.put("msg","您已经参与了");
        } else if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }


}
