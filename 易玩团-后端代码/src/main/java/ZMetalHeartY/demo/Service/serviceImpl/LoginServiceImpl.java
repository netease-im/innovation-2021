package ZMetalHeartY.demo.Service.serviceImpl;


import ZMetalHeartY.demo.Entity.FrontUser;
import ZMetalHeartY.demo.Entity.FrontUserFollow;
import ZMetalHeartY.demo.Mapper.FrontUserFollowMapper;
import ZMetalHeartY.demo.Mapper.FrontUserMapper;
import ZMetalHeartY.demo.Propertie.FilePathProperties;
import ZMetalHeartY.demo.Service.LoginService;
import ZMetalHeartY.demo.Utils.LoginUtils;
import com.alibaba.fastjson.JSON;
import net.coobird.thumbnailator.Thumbnails;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private FrontUserMapper frontUserMapper;
    @Autowired
    private LoginUtils loginUtils;
    @Autowired
    private FilePathProperties filePathProperties;
    @Autowired
    private FrontUserFollowMapper frontUserFollowMapper;


    @Override
    public String weChatLogin(String jsCode) {
        String loginInfo = null;
        JSONObject json = null;
        try {
            loginInfo = loginUtils.getWeChatloginInfo(jsCode);
            json = new JSONObject(loginInfo);
            FrontUser frontUser = frontUserMapper.getByOpenid((String) json.get("openid"));
            if (frontUser != null){
                json.put("userinfo", JSON.toJSONString(frontUser));
            }else{
                json.put("userinfo","null");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return json.toString();
    }



    @Override
    public String getUserByFuId(Integer fuId,Integer nFuId){
        FrontUser frontUser = frontUserMapper.selectByPrimaryKey(fuId);
        JSONObject json = new JSONObject();
        if (frontUser != null){
            FrontUserFollow frontUserFollow = new FrontUserFollow();
            json = new JSONObject(frontUser);
            json.remove("openid");

            frontUserFollow.setFuId(fuId);
            json.put("followedCount",frontUserFollowMapper.selectAllByCondition(frontUserFollow).size());

            frontUserFollow = new FrontUserFollow();
            frontUserFollow.setFufFuId(fuId);
            json.put("isFollowedCount",frontUserFollowMapper.selectAllByCondition(frontUserFollow).size());

            if (!fuId.equals(nFuId)){
                frontUserFollow.setFuId(nFuId);
                json.put("isFocus", frontUserFollowMapper.selectAllByCondition(frontUserFollow).size() > 0);
            }


        }else {
            json.put("status","null");
        }


        return json.toString();
    }


    private final static Object LOCK = 1;

    @Override
    public String WeChatUpdateUserInfo(FrontUser frontUser) {

        synchronized (Objects.requireNonNull(LOCK)) {
            frontUser.setFuStatus(1);
            if (frontUser.getFuCredit() == null) {
                frontUser.setFuCredit(100);
            }

            int c = 0;
            JSONObject json = new JSONObject();
            if (frontUserMapper.exist(frontUser) > 0) {//openid
                c++;
            } else {

                String imgPath = null;
                try {
                    URL url = new URL(frontUser.getFuImg());
                    URLConnection con = url.openConnection();
                    InputStream is = con.getInputStream();
                    byte[] bs = new byte[1024000];
                    int len;

                    String fileName = UUID.randomUUID() + "";

                    imgPath = filePathProperties.getUserImgsPath() + fileName + ".gif";
                    String savePath = filePathProperties.getUserImgsDocBase() + fileName + ".gif";  //下载路径及下载图片名称

                    File file = new File(savePath);
                    FileOutputStream os = new FileOutputStream(file, true);
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    os.close();
                    is.close();

                    Thumbnails.of(file).size(140, 140).keepAspectRatio(false).toFile(savePath);//转换图片大小


                } catch (Exception e) {
                    //e.printStackTrace();
                }
                frontUser.setFuImg(imgPath);
                Date date = new Date();
                frontUser.setFuCreateTime(new Timestamp(date.getTime()));
                
                c += frontUserMapper.insert(frontUser);//openid, nickname,sex, headphoto
            }


            if (c > 0) {

                json.put("userid", frontUserMapper.getByOpenid(frontUser.getFuOpenid()).getFuId());
                json.put("status", "succ");
            } else {
                json.put("status", "error");
            }

            return json.toString();
        }
    }


}
