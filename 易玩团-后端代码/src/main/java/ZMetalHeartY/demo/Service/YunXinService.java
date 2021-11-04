package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.FrontUser;
import ZMetalHeartY.demo.Entity.YunXinMessage;
import ZMetalHeartY.demo.Entity.YunXinResponse;
import ZMetalHeartY.demo.Propertie.YunXinProperties;
import ZMetalHeartY.demo.Utils.NetEaseYunXin.CheckSumBuilder;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class YunXinService {

    @Autowired
    private YunXinProperties yunXinProperties;

    public YunXinResponse create(FrontUser frontUser) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/create.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = yunXinProperties.getAppKey();
        String appSecret = yunXinProperties.getAppSecret();
        String nonce =  UUID.randomUUID().toString().replaceAll("-","");
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", frontUser.getFuId()+""));
        nvps.add(new BasicNameValuePair("name", frontUser.getFuOpenid()));//用户名
        nvps.add(new BasicNameValuePair("icon", frontUser.getFuImg()));//头像
        nvps.add(new BasicNameValuePair("sign", frontUser.getFuNote()));//个性签名
        /*nvps.add(new BasicNameValuePair("email", "null"));//邮箱
        nvps.add(new BasicNameValuePair("birth", "null"));//生日*/
        nvps.add(new BasicNameValuePair("mobile", frontUser.getFuPhone())); //手机号
        nvps.add(new BasicNameValuePair("gender", frontUser.getFuSex()+"")); //性别
        nvps.add(new BasicNameValuePair("ex", ""));//拓展内容，最好用json




        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        return JSON.parseObject(EntityUtils.toString(response.getEntity()),YunXinResponse.class);
    }

    public YunXinResponse update(FrontUser frontUser) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/update.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = yunXinProperties.getAppKey();
        String appSecret = yunXinProperties.getAppSecret();
        String nonce =  UUID.randomUUID().toString().replaceAll("-","");
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", "helloworld"));
        nvps.add(new BasicNameValuePair("name", "helloworld2"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        return JSON.parseObject(EntityUtils.toString(response.getEntity()),YunXinResponse.class);
    }

    public YunXinResponse refreshToken(FrontUser frontUser) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/refreshToken.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = yunXinProperties.getAppKey();
        String appSecret = yunXinProperties.getAppSecret();
        String nonce =  UUID.randomUUID().toString().replaceAll("-","");
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", frontUser.getFuId()+""));
        nvps.add(new BasicNameValuePair("name", frontUser.getFuOpenid()));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        return JSON.parseObject(EntityUtils.toString(response.getEntity()),YunXinResponse.class);
    }

    public YunXinResponse sendMsg(YunXinMessage yunXinMessage) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/msg/sendMsg.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = yunXinProperties.getAppKey();
        String appSecret = yunXinProperties.getAppSecret();
        String nonce =  UUID.randomUUID().toString().replaceAll("-","");
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码


        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("from", yunXinMessage.getFrom()));
        nvps.add(new BasicNameValuePair("ope", yunXinMessage.getOpe()+""));
        nvps.add(new BasicNameValuePair("to", yunXinMessage.getTo()));
        nvps.add(new BasicNameValuePair("type", yunXinMessage.getType()+""));
        nvps.add(new BasicNameValuePair("body", yunXinMessage.getBody()));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        return JSON.parseObject(EntityUtils.toString(response.getEntity()),YunXinResponse.class);
    }


}
