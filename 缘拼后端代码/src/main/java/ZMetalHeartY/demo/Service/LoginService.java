package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.FrontUser;

public interface LoginService {

    //微信登陆
    String weChatLogin(String jsCode);

    String getUserByFuId(Integer fuId,Integer nFuId);

    String WeChatUpdateUserInfo(FrontUser frontUser);

}
