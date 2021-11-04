package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.FrontUserIssueLike;
import ZMetalHeartY.demo.Entity.Issue;
import ZMetalHeartY.demo.Entity.News;
import ZMetalHeartY.demo.Mapper.FrontUserIssueLikeMapper;
import ZMetalHeartY.demo.Mapper.IssueMapper;
import ZMetalHeartY.demo.Mapper.NewsMapper;
import ZMetalHeartY.demo.Service.FrontUserIssueLikeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontUserIssueLikeServiceImpl implements FrontUserIssueLikeService {

    FrontUserIssueLikeMapper frontUserIssueLikeMapper;
    NewsMapper newsMapper;
    IssueMapper issueMapper;

    public FrontUserIssueLikeServiceImpl(FrontUserIssueLikeMapper frontUserIssueLikeMapper, NewsMapper newsMapper, IssueMapper issueMapper) {
        this.frontUserIssueLikeMapper = frontUserIssueLikeMapper;
        this.newsMapper = newsMapper;
        this.issueMapper = issueMapper;
    }

    @Override
    public int deleteByFuilId(Integer fuId, Integer iId) {
        return frontUserIssueLikeMapper.deleteByPrimaryKey(fuId, iId);
    }

    @Override
    public int insert(FrontUserIssueLike frontUserIssueLike) {
        int ret = frontUserIssueLikeMapper.insertSelective(frontUserIssueLike);
        Issue issue = issueMapper.selectByPrimaryKey(frontUserIssueLike.getiId());
        News news = new News();
        news.setFromUserId(frontUserIssueLike.getFuId());
        news.setToUserId(issue.getFuId());
        news.setnMsg("有人点赞了您的帖子");
        news.setiId(frontUserIssueLike.getiId());
        news.setnType(1);
        news.setnIsRead(1);
        newsMapper.insert(news);
        return ret;
    }

    @Override
    public FrontUserIssueLike selectByFuilId(Integer fuilId) {

        return frontUserIssueLikeMapper.selectByPrimaryKey(fuilId);
    }

    @Override
    public PageInfo<FrontUserIssueLike> getFrontUserIssueLikeByPage(FrontUserIssueLike frontUserIssueLike, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<FrontUserIssueLike> frontUserIssueLikes = frontUserIssueLikeMapper.selectAllByCondition(frontUserIssueLike);
        PageInfo<FrontUserIssueLike> frontUserIssueLikePageInfo = new PageInfo<>(frontUserIssueLikes);
        return frontUserIssueLikePageInfo;
    }

    @Override
    public int updateByFuilIdSelective(FrontUserIssueLike record) {
        return frontUserIssueLikeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<FrontUserIssueLike> getAllFrontUserIssueLike(FrontUserIssueLike frontUserIssueLike) {
        return frontUserIssueLikeMapper.selectAllByCondition(frontUserIssueLike);
    }
}
