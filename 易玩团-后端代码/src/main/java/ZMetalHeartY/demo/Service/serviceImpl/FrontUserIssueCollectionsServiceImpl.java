package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.FrontUserIssueCollections;
import ZMetalHeartY.demo.Entity.Issue;
import ZMetalHeartY.demo.Mapper.FrontUserIssueCollectionsMapper;
import ZMetalHeartY.demo.Mapper.IssueMapper;
import ZMetalHeartY.demo.Service.FrontUserIssueCollectionsService;
import ZMetalHeartY.demo.Service.IssueService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontUserIssueCollectionsServiceImpl implements FrontUserIssueCollectionsService {

    FrontUserIssueCollectionsMapper frontUserIssueCollectionsMapper;
    IssueService issueService;

    public FrontUserIssueCollectionsServiceImpl(FrontUserIssueCollectionsMapper frontUserIssueCollectionsMapper, IssueService issueService) {
        this.frontUserIssueCollectionsMapper = frontUserIssueCollectionsMapper;
        this.issueService = issueService;
    }

    @Override
    public int deleteByFuicId(Integer fuId, Integer iId) {
        return frontUserIssueCollectionsMapper.deleteByPrimaryKey(fuId,iId);
    }

    @Override
    public int insert(FrontUserIssueCollections frontUserIssueCollections) {
        return frontUserIssueCollectionsMapper.insertSelective(frontUserIssueCollections);
    }

    @Override
    public FrontUserIssueCollections selectByFuicId(Integer fuicId) {
        return frontUserIssueCollectionsMapper.selectByPrimaryKey(fuicId);
    }

    @Override
    public PageInfo<FrontUserIssueCollections> getFrontUserIssueCollectionsByPage(FrontUserIssueCollections frontUserIssueCollections, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<FrontUserIssueCollections> frontUserIssueCollectionsList = frontUserIssueCollectionsMapper.selectAllByCondition(frontUserIssueCollections);
        for (FrontUserIssueCollections frontUserIssueCollections1:frontUserIssueCollectionsList) {
            issueService.getGetIssueContent(frontUserIssueCollections1.getIssue(),frontUserIssueCollections1.getFuId());
        }

        PageInfo<FrontUserIssueCollections> frontUserIssueCollectionsPageInfo = new PageInfo<>(frontUserIssueCollectionsList);
        return frontUserIssueCollectionsPageInfo;
    }

    @Override
    public int updateByFuicIdSelective(FrontUserIssueCollections record) {
        return frontUserIssueCollectionsMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<FrontUserIssueCollections> getAllFrontUserIssueCollections(FrontUserIssueCollections frontUserIssueCollections) {
        return frontUserIssueCollectionsMapper.selectAllByCondition(frontUserIssueCollections);
    }
}
