package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.FrontUserIssueCollections;
import ZMetalHeartY.demo.Entity.Issue;
import ZMetalHeartY.demo.Entity.IssueAttend;
import ZMetalHeartY.demo.Entity.News;
import ZMetalHeartY.demo.Mapper.IssueAttendMapper;
import ZMetalHeartY.demo.Mapper.IssueMapper;
import ZMetalHeartY.demo.Mapper.NewsMapper;
import ZMetalHeartY.demo.Service.IssueAttendService;
import ZMetalHeartY.demo.Service.IssueService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IssueAttendServiceImpl implements IssueAttendService {

    IssueAttendMapper issueAttendMapper;
    IssueMapper issueMapper;
    NewsMapper newsMapper;
    IssueService issueService;

    public IssueAttendServiceImpl(IssueAttendMapper issueAttendMapper, IssueMapper issueMapper, NewsMapper newsMapper, IssueService issueService) {
        this.issueAttendMapper = issueAttendMapper;
        this.issueMapper = issueMapper;
        this.newsMapper = newsMapper;
        this.issueService = issueService;
    }

    @Override
    public int deleteByIaId(Integer iaId) {
        return issueAttendMapper.deleteByPrimaryKey(iaId);
    }

    @Override
    public int insert(IssueAttend issueAttend) {

        Integer iId = issueAttend.getiId();
        Issue issue = issueMapper.selectByPrimaryKey(iId);


        if(issue.getiStatus() == 4){
            return -1;
        }

        if(issueAttendMapper.isAttend(issueAttend.getFuId(),issueAttend.getiId())){
            return -2;
        }

        issueAttend.setIaStatus(1);//默认无需审核，直接加入

        int ret = issueAttendMapper.insertSelective(issueAttend);
        List<IssueAttend> issueAttends = issueAttendMapper.selectByIId(iId);

        if(issueAttends.size() >= issue.getiPeopleNumber()){
            issueMapper.complete(iId);
        }


        //发送消息
        News news = new News();
        news.setFromUserId(issueAttend.getFuId());
        news.setToUserId(issue.getFuId());
        news.setToCommentId(issueAttend.getIaId());
        news.setnMsg("有人想参与你的拼团");
        news.setiId(issue.getiId());
        news.setnType(4);
        newsMapper.insert(news);


        return ret;
    }


    public int adopt(Integer iaId){


        IssueAttend issueAttend = issueAttendMapper.selectByPrimaryKey(iaId);
        Issue issue = issueMapper.selectByPrimaryKey(issueAttend.getiId());
        issueAttend.setIaStatus(2);
        issueAttendMapper.updateByPrimaryKeySelective(issueAttend);

        //发送消息
        News news = new News();
        news.setFromUserId(issueAttend.getFuId());
        news.setToUserId(issue.getFuId());
        news.setnMsg("通过了你的申请");
        news.setiId(issue.getiId());
        news.setnIsRead(1);
        news.setnType(5);
        newsMapper.insert(news);


        return 1;
    }

    public int refuse(Integer iaId){

        IssueAttend issueAttend = issueAttendMapper.selectByPrimaryKey(iaId);
        Issue issue = issueMapper.selectByPrimaryKey(issueAttend.getiId());
        issueAttend.setIaStatus(3);
        issueAttendMapper.updateByPrimaryKeySelective(issueAttend);

        //发送消息
        News news = new News();
        news.setFromUserId(issueAttend.getFuId());
        news.setToUserId(issue.getFuId());
        news.setnMsg("拒绝了你的申请");
        news.setiId(issue.getiId());
        news.setnIsRead(1);
        news.setnType(5);
        newsMapper.insert(news);


        return 1;
    }

    @Override
    public IssueAttend selectByIaId(Integer iaId) {
        return issueAttendMapper.selectByPrimaryKey(iaId);
    }

    @Override
    public PageInfo<IssueAttend> getIssueAttendByPage(IssueAttend issueAttend, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<IssueAttend> issueAttends = issueAttendMapper.selectAllByCondition(issueAttend);
        for (IssueAttend issueAttend1:issueAttends) {

            issueService.getGetIssueContent(issueAttend1.getIssue(),issueAttend1.getFuId());

            issueAttend1.getIssue().setFrontUser(issueAttend1.getFrontUser());
        }
        PageInfo<IssueAttend> issueAttendPageInfo = new PageInfo<>(issueAttends);
        return issueAttendPageInfo;
    }

    @Override
    public int updateByIaIdSelective(IssueAttend record) {
        return issueAttendMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<IssueAttend> getAllIssueAttend(IssueAttend issueAttend) {
        return issueAttendMapper.selectAllByCondition(issueAttend);
    }
}
