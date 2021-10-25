package ZMetalHeartY.demo.Utils;

import ZMetalHeartY.demo.Propertie.WeChatProperties;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.AlgorithmParameterSpec;

@Component
public class LoginUtils {

    @Autowired
    WeChatProperties weChatProperties;

    public String getWeChatloginInfo(String js_code) throws IOException {

        System.out.println(weChatProperties.getAppid()+"---"+weChatProperties.getSecret());

        String url_str = "https://api.weixin.qq.com/sns/jscode2session?appid="+weChatProperties.getAppid()+"&secret="+weChatProperties.getSecret()+"&js_code="+js_code+"&grant_type=authorization_code";
        URL url = new URL(url_str);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream is = connection.getInputStream();
        byte[] response = new byte[is.available()];
        is.read(response);
        is.close();

        String loginInfo = new String(response, "UTF-8");

        return loginInfo;
    }


    public String decrypt(String keyStr, String ivStr, String encDataStr)throws Exception {

        byte[] encData = Base64.decode(encDataStr);
        byte[] iv = Base64.decode(ivStr);
        byte[] key = Base64.decode(keyStr);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);



        return new String(cipher.doFinal(encData),"UTF-8");
    }

}
