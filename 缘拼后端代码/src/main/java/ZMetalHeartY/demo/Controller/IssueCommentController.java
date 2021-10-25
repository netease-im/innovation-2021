package ZMetalHeartY.demo.Controller;

import ZMetalHeartY.demo.Entity.IssueComment;
import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Service.IssueCommentService;
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
@RequestMapping("/issueComment")
public class IssueCommentController {

    @Autowired
    IssueCommentService issueCommentService;

    @RequestMapping("/list")
    public String list(){
        return "IssueComment/issueCommentList";
    }

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(IssueComment issueComment, Integer page, Integer limit){
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session=request.getSession();
        User Account=(User) session.getAttribute("user");

        PageInfo<IssueComment> issueCommentPageInfo = issueCommentService.getIssueCommentByPage(issueComment,page,limit);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",issueCommentPageInfo.getList());
        json.put("count",issueCommentPageInfo.getTotal());
        return json;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(Integer icId){
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = issueCommentService.deleteByIcId(icId);
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
    public String echodata(@RequestParam("icId") Integer icId, Model model){
        IssueComment issueComment = issueCommentService.selectByIcId(icId);
        model.addAttribute("issueComment",issueComment);
        return "IssueComment/issueCommentEdit";
    }

    @RequestMapping("/addpage")
    public String addpage(IssueComment issueComment, Integer id, Model model) {
        return "IssueComment/issueCommentAdd";
    }

    @RequestMapping("/details")
    public String detail(@RequestParam("icId") Integer icId, Model model) {

        IssueComment issueComment = issueCommentService.selectByIcId(icId);
        model.addAttribute("issueComment",issueComment);
        return "IssueComment/issueCommentDetail";
    }

    @RequestMapping("/getDetails")
    public String getDetails(@RequestParam("icId") Integer icId, Model model) {

        JSONObject json = new JSONObject();
        IssueComment issueComment = issueCommentService.selectByIcId(icId);

        if(issueComment != null){
            json.put("msg","");
            json.put("code","0");
            json.put("data",issueComment);
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
    public JSONObject update(IssueComment issueComment){
        System.out.println(JSON.toJSONString(issueComment));
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = issueCommentService.updateByIcIdSelective(issueComment);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }


    @RequestMapping("/add")
    @ResponseBody
    public JSONObject insert(IssueComment issueComment){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();

        ret = issueCommentService.insert(issueComment);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }
    
}
