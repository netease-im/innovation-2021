package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.FrontUser;
import ZMetalHeartY.demo.Entity.FrontUserIssueCollections;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FrontUserIssueCollectionsService {
    
    int deleteByFuicId(Integer fuId, Integer iId);

    int insert(FrontUserIssueCollections frontUserIssueCollections);

    FrontUserIssueCollections selectByFuicId(Integer fuicId);

    PageInfo<FrontUserIssueCollections> getFrontUserIssueCollectionsByPage(FrontUserIssueCollections frontUserIssueCollections, Integer page, Integer limit);

    int updateByFuicIdSelective(FrontUserIssueCollections record);

    List<FrontUserIssueCollections> getAllFrontUserIssueCollections(FrontUserIssueCollections frontUserIssueCollections);
    
}
