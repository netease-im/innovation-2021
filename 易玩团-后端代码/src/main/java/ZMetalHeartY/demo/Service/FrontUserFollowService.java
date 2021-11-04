package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.FrontUserFollow;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FrontUserFollowService {
    
    int delete(Integer fuId,Integer fufFuId);

    int insert(FrontUserFollow frontUserFollow);

    FrontUserFollow selectByFufId(Integer fufId);

    PageInfo<FrontUserFollow> getFrontUserFollowByPage(FrontUserFollow frontUserFollow, Integer page, Integer limit);

    int updateByFufIdSelective(FrontUserFollow record);

    List<FrontUserFollow> getAllFrontUserFollow(FrontUserFollow frontUserFollow);
    
}
