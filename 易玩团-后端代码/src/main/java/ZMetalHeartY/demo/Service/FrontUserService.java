package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.FrontUser;
import ZMetalHeartY.demo.Entity.FrontUser;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZMetalHeartY
 */
@Service
public interface FrontUserService {
    
    int deleteByFuId(Integer fuId);

    int insert(FrontUser frontUser);

    FrontUser selectByFuId(Integer fuId);

    PageInfo<FrontUser> getFrontUserByPage(FrontUser frontUser, Integer page, Integer limit);

    int updateByFuIdSelective(FrontUser record);

    List<FrontUser> getAllFrontUser(FrontUser frontUser);
    
}
