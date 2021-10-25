package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.FrontUserFollow;
import ZMetalHeartY.demo.Entity.Issue;
import ZMetalHeartY.demo.Entity.News;
import ZMetalHeartY.demo.Mapper.FrontUserFollowMapper;
import ZMetalHeartY.demo.Mapper.FrontUserMapper;
import ZMetalHeartY.demo.Mapper.NewsMapper;
import ZMetalHeartY.demo.Service.FrontUserFollowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontUserFollowServiceImpl implements FrontUserFollowService {

    FrontUserFollowMapper frontUserFollowMapper;
    NewsMapper newsMapper;

    public FrontUserFollowServiceImpl(FrontUserFollowMapper frontUserFollowMapper, NewsMapper newsMapper) {
        this.frontUserFollowMapper = frontUserFollowMapper;
        this.newsMapper = newsMapper;
    }

    @Override
    public int delete(Integer fuId,Integer fufFuId) {
        return frontUserFollowMapper.deleteByPrimaryKey(fuId,fufFuId);
    }

    @Override
    public int insert(FrontUserFollow frontUserFollow) {

        int ret = frontUserFollowMapper.insertSelective(frontUserFollow);
        News news = new News();
        news.setFromUserId(frontUserFollow.getFuId());
        news.setToUserId(frontUserFollow.getFufId());
        news.setnMsg("有人关注了您");
        news.setnType(3);
        news.setnIsRead(1);
        newsMapper.insert(news);
        return ret;
    }

    @Override
    public FrontUserFollow selectByFufId(Integer fufId) {
        return frontUserFollowMapper.selectByPrimaryKey(fufId);
    }

    @Override
    public PageInfo<FrontUserFollow> getFrontUserFollowByPage(FrontUserFollow frontUserFollow, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<FrontUserFollow> frontUserFollows = frontUserFollowMapper.selectAllByCondition(frontUserFollow);
        PageInfo<FrontUserFollow> frontUserFollowsPageInfo = new PageInfo<>(frontUserFollows);
        return frontUserFollowsPageInfo;
    }

    @Override
    public int updateByFufIdSelective(FrontUserFollow record) {
        return frontUserFollowMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<FrontUserFollow> getAllFrontUserFollow(FrontUserFollow frontUserFollow) {
        List<FrontUserFollow> frontUserFollows = frontUserFollowMapper.selectAllByCondition(frontUserFollow);

        if(frontUserFollow.getFuId() == null){
            for (FrontUserFollow frontUserFollow1:frontUserFollows) {
                FrontUserFollow fuf = new FrontUserFollow();
                fuf.setFuId(frontUserFollow.getFufFuId());
                fuf.setFufFuId(frontUserFollow1.getFuId());
                if(frontUserFollowMapper.selectAllByCondition(fuf).size() > 0){
                    frontUserFollow1.setIsFollow(true);
                }else{
                    frontUserFollow1.setIsFollow(false);
                }
            }
        }else if(frontUserFollow.getFufFuId() == null){
            for (FrontUserFollow frontUserFollow1:frontUserFollows) {
                frontUserFollow1.setIsFollow(true);
            }
        }



        return frontUserFollows;
    }
}
