package sample.ServiceHandler;


import Core.annotation.Inject;
import Core.annotation.ServiceHandlerMapping;
import Core.handlers.HttpRequestHandler;
import Core.handlers.HttpResponseHandler;
import Core.net.NettyGroup;
import Core.template.ServiceHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import sample.Beans.*;
import sample.File.MultipartRequest;
import sample.Service.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ServiceHandlerMapping("")
public class ServiceTest extends ServiceHandler {

    @Inject
    UserService userService;

    @Inject
    planService planService;

    @Inject
    EmailService emailService;

    @Inject
    goalService goalService;

    @Inject
    noticeService noticeService;

    @Inject
    coworkService coworkService;

    @Inject
    fileService fileService;


    @ServiceHandlerMapping("/uploadCowork")
    public void uploadCowork(HttpResponseHandler res, cowork c) {
        if (coworkService.addCowork(c)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/downloadCowork")
    public void downloadCowork(HttpResponseHandler res, HttpRequestHandler req) {
        int partner = Integer.parseInt((String) req.getParameter("partner"));
        res.write(coworkService.getCowork(partner));
    }


    @ServiceHandlerMapping("/deleteCowork")
    public void deleteCowork(HttpResponseHandler res, HttpRequestHandler req) {
        int id = Integer.parseInt((String) req.getParameter("id"));
        if (coworkService.deleteCowork(id)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/uploadNotice")
    public void uploadNotice(HttpResponseHandler res, notice n) {
        if (noticeService.addNotice(n)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/downloadNotice")
    public void downloadNotice(HttpResponseHandler res, HttpRequestHandler req) {
        int user_id = Integer.parseInt((String) req.getParameter("user_id"));
        res.write(noticeService.getNotice(user_id));
    }

    @ServiceHandlerMapping("/deleteNotice")
    public void deleteNotice(HttpResponseHandler res, HttpRequestHandler req) {
        int id = Integer.parseInt((String) req.getParameter("id"));
        if (noticeService.deleteNotice(id)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/updateNotice")
    public void updateNotice(HttpResponseHandler res, notice n) {
        if (noticeService.updateNoticeContent(n)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/updateNoticeRead")
    public void updateNoticeRead(HttpResponseHandler res, notice n) {
        if (noticeService.updateNoticeRead(n)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }


    @ServiceHandlerMapping("/uploadGoal")
    public void uploadGoal(HttpResponseHandler res, goal g) {
        if (goalService.insert(g)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/downloadGoal")
    public void downloadGoal(HttpResponseHandler res, HttpRequestHandler req) {
        int user_id = Integer.parseInt((String) req.getParameter("user_id"));
        res.write(goalService.getGoal(user_id));
    }

    @ServiceHandlerMapping("/deleteGoal")
    public void deleteGoal(HttpResponseHandler res, goal g) {
        if (goalService.deleteGoal(g.getUser_id(), g.getGoal_name())) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/register")
    public void register(HttpResponseHandler res, HttpRequestHandler req, user u) {
        if (!userService.isUnique(u)) {
            res.write("exists");
            return;
        }
        if (userService.addUser(u)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }


    @ServiceHandlerMapping("/login")
    public void login(HttpResponseHandler res, HttpRequestHandler req, user u) {
        user user = userService.checkUser(u);
        if (user != null) {
            res.write(JSON.toJSONString(user));
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/getUserId")
    public void getUserId(HttpResponseHandler res, HttpRequestHandler req, user u) {
        user user = userService.getUser(u.getEmail());
        if (user != null) {
            res.write(JSON.toJSONString(user));
        } else {
            res.write("-1");
        }
    }

    @ServiceHandlerMapping("/changeUserInfo")
    public void changeUserInfo(HttpResponseHandler res, user u) {
        if (userService.updateUser(u)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/updateUserContacts")
    public void updateUserContacts(HttpResponseHandler res, user u) {
        if (userService.updateUserContacts(u)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/deletePlan")
    public void deletePlan(HttpRequestHandler req, HttpResponseHandler res) {
        int user_id = Integer.parseInt((String) req.getParameter("user_id"));
        String plan_name = (String) req.getParameter("plan_name");

        if (planService.deletePlan(user_id, plan_name)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/uploadPlan")
    public void upload(HttpRequestHandler req, HttpResponseHandler res) {
        String plan = (String) req.getParameter("plan");
        String planLog = (String) req.getParameter("planLog");
        int user_id = Integer.parseInt((String) req.getParameter("user_id"));

        JSONArray planArray = JSON.parseArray(plan);
        JSONArray planLogArray = JSON.parseArray(planLog);

        if (planArray.size() != planLogArray.size() || user_id == -1) {
            res.write("fail");
        }

        int l = planArray.size();

        for (int i = 0; i < l; i++) {
            plan bean = new plan();
            bean.setUser_id(user_id);

            JSONObject planData = (JSONObject) planArray.get(i);

            String plan_text = planData.toString();
            String planLog_text = planLogArray.get(i).toString();
            bean.setPlan_text(plan_text);
            bean.setPlanLog_text(planLog_text);

            JSONObject planInfo = (JSONObject) planData.get("plan");
            bean.setPlan_name((String) planInfo.get("name"));

            if (planService.insert(bean)) {
                res.write("success");
            } else {
                res.write("fail");
            }
        }
    }

    @ServiceHandlerMapping("/downloadPlan")
    public void downloadPlan(HttpRequestHandler req, HttpResponseHandler res) {
        int user_id = Integer.parseInt((String) req.getParameter("user_id"));
        String jsonStr = planService.getPlan(user_id);
        res.write(jsonStr);
    }

    @ServiceHandlerMapping("/updatePlan")
    public void updatePlan(HttpResponseHandler res, plan p) {
        if (planService.updatePlanById(p)) {
            res.write("success");
        } else {
            res.write("fail");
        }
    }


    @ServiceHandlerMapping("/getPlanId")
    public void getPlanId(HttpResponseHandler res, plan p) {
        int id = planService.getPlanId(p);
        res.write(Integer.toString(id));
    }


    @ServiceHandlerMapping("/emailSync")
    public void doEmailSync(HttpRequestHandler req, HttpResponseHandler res) {
        int user_id = Integer.parseInt((String) req.getParameter("user_id"));
        String acceptor_name = (String) req.getParameter("user_name");
        user user = userService.getUserEmail(user_id);
        InputStream in = this.getClass().getResourceAsStream("/static/page/email.html");
        String content = null;
        try {
            assert in != null;
            content = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (user == null) {
            res.write("fail");
        }
        assert user != null;
        String email = user.getEmail();

        String schedule = (String) req.getParameter("schedule");
        JSONArray arr = JSON.parseArray(schedule);
        for (Object o : arr) {
            JSONObject j = (JSONObject) o;
            assert content != null;
            String start = (String) j.get("start");
            String startContent = (String) j.get("startContent");
            String end = (String) j.get("end");
            String endContent = (String) j.get("endContent");
            if (start != null && startContent != null) {

                Document html = Jsoup.parse(content);
                Element acceptor = html.getElementById("acceptor");
                acceptor.append(acceptor_name + ",");
                Element body = html.getElementById("bodyContent");
                body.append(startContent);
                emailService.addJob(start, html.toString(), email);
            }
            if (end != null && endContent != null) {
                Document html = Jsoup.parse(content);
                Element acceptor = html.getElementById("acceptor");
                acceptor.append(acceptor_name + ",");
                Element body = html.getElementById("bodyContent");
                body.append(endContent);
                emailService.addJob(end, html.toString(), email);
            }
        }
        res.write("success");
    }


    @ServiceHandlerMapping("/chkOnline")
    public void chkOnline(HttpRequestHandler req, HttpResponseHandler res) {
        String userArr = (String) req.getParameter("userArr");
        List<Integer> users = JSON.parseArray(userArr, Integer.class);
        Map<String, Integer> status = new HashMap<>();
        for (Integer user_id : users) {
            if (NettyGroup.user.containsKey(user_id)) {
                ChannelId cid = NettyGroup.user.get(user_id);
                Channel c = NettyGroup.group.find(cid);
                if (c != null && c.isActive()) {
                    status.put(Integer.toString(user_id), 1);
                } else {
                    status.put(Integer.toString(user_id), 0);
                    NettyGroup.user.remove(user_id);
                }
            } else {
                status.put(Integer.toString(user_id), 0);
            }
        }
        res.write(JSON.toJSONString(status));
    }


    @ServiceHandlerMapping("/uploadFile")
    public void uploadFile(HttpRequestHandler req, HttpResponseHandler res) {
        for (Object x : req.params.values()) {
            InterfaceHttpData a = (InterfaceHttpData) x;
            if (InterfaceHttpData.HttpDataType.FileUpload.equals(a.getHttpDataType())) {
                FileUpload fileUpload = (FileUpload) a;
                try {
                    fileService.storeFile(fileUpload.getFilename(),fileUpload.get(), fileUpload.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ServiceHandlerMapping("/downloadFile")
    public void downLoadFile(HttpRequestHandler req, HttpResponseHandler res) {
        String id = (String) req.getParameter("id");
        String fileName = (String) req.getParameter("fileName");
        try {
            if (!fileService.downLoadFile(res, id, fileName)) {
                res.writeStatus(HttpResponseStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        res.writeStatus(HttpResponseStatus.NOT_FOUND);
    }

    @ServiceHandlerMapping("/deleteFile")
    public void deleteFile(HttpRequestHandler req, HttpResponseHandler res){
        String id = (String) req.getParameter("id");
        String fileName = (String) req.getParameter("fileName");
        if(fileService.deleteFile(id,fileName)){
            res.write("success");
        }else{
            res.write("fail");
        }
    }

    @ServiceHandlerMapping("/getFileInfo")
    public void getFileInfo(HttpRequestHandler req, HttpResponseHandler res){
        String id=(String) req.getParameter("id");
        res.write(fileService.getFileInfo(id));
    }




}
