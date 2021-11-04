package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.FrontUser;
import ZMetalHeartY.demo.Entity.FrontUserIssueLike;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FrontUserIssueLikeService {
    
    int deleteByFuilId(Integer fuId, Integer iId) ;

    int insert(FrontUserIssueLike frontUserIssueLike);

    FrontUserIssueLike selectByFuilId(Integer fuilId);

    PageInfo<FrontUserIssueLike> getFrontUserIssueLikeByPage(FrontUserIssueLike frontUserIssueLike, Integer page, Integer limit);

    int updateByFuilIdSelective(FrontUserIssueLike record);

    List<FrontUserIssueLike> getAllFrontUserIssueLike(FrontUserIssueLike frontUserIssueLike);
    
}
