package ZMetalHeartY.demo.Controller.WeChat;

import ZMetalHeartY.demo.Entity.FrontUser;
import ZMetalHeartY.demo.Entity.YunXinMessage;
import ZMetalHeartY.demo.Entity.YunXinResponse;
import ZMetalHeartY.demo.Service.YunXinService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;


@Slf4j
@Controller
@RequestMapping("/WeChat/yunXin")
@ResponseBody
public class WeChatYunXinLogin {

    @Autowired
    YunXinService yunXinService;

    HashMap<Integer,String> tokenMap = new HashMap<>();

    @RequestMapping("/login")
    public String login(FrontUser frontUser) throws Exception {

        if(tokenMap.containsKey(frontUser.getFuId())){
            return tokenMap.get(frontUser.getFuId());
        }

        log.info(JSON.toJSONString(frontUser));
        YunXinResponse yunXinResponse = yunXinService.create(frontUser);
        if(!"200".equals(yunXinResponse.getCode())){
            yunXinResponse = yunXinService.refreshToken(frontUser);
        }
        tokenMap.put(frontUser.getFuId(),JSON.toJSONString(yunXinResponse));

        return JSON.toJSONString(yunXinResponse);
    }


    @RequestMapping("/sendMsg")
    public String sendMsg(YunXinMessage yunXinMessage) throws Exception {

        log.info(JSON.toJSONString(yunXinMessage));
        YunXinResponse yunXinResponse = yunXinService.sendMsg(yunXinMessage);
        return JSON.toJSONString(yunXinResponse);
    }


    @RequestMapping("/update")
    public String update(FrontUser frontUser) throws Exception {

        log.info(JSON.toJSONString(frontUser));
        YunXinResponse yunXinResponse = yunXinService.update(frontUser);
        return JSON.toJSONString(yunXinResponse);
    }



}
