package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.Issue;
import ZMetalHeartY.demo.Entity.IssueComment;
import ZMetalHeartY.demo.Entity.News;
import ZMetalHeartY.demo.Mapper.IssueCommentMapper;
import ZMetalHeartY.demo.Mapper.IssueMapper;
import ZMetalHeartY.demo.Mapper.NewsMapper;
import ZMetalHeartY.demo.Service.IssueCommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueCommentServiceImpl implements IssueCommentService {

    IssueCommentMapper issueCommentMapper;
    IssueMapper issueMapper;
    NewsMapper newsMapper;

    public IssueCommentServiceImpl(IssueCommentMapper issueCommentMapper, IssueMapper issueMapper, NewsMapper newsMapper) {
        this.issueCommentMapper = issueCommentMapper;
        this.issueMapper = issueMapper;
        this.newsMapper = newsMapper;
    }

    @Override
    public int deleteByIcId(Integer icId) {
        return issueCommentMapper.deleteByPrimaryKey(icId);
    }

    @Override
    public int insert(IssueComment issueComment) {
        //因为帖子评论现在是一级评论所以写的这么简单，如果后期改成多级评论的话这里要改
        int ret = issueCommentMapper.insertSelective(issueComment);
        Issue issue = issueMapper.selectByPrimaryKey(issueComment.getiId());
        News news = new News();
        news.setFromUserId(issueComment.getFuId());
        news.setToUserId(issue.getFuId());
        news.setnMsg(issueComment.getIcMsg());
        news.setiId(issueComment.getiId());
        news.setnType(2);
        news.setnIsRead(1);
        newsMapper.insert(news);
        return ret;
    }

    @Override
    public IssueComment selectByIcId(Integer icId) {
        return issueCommentMapper.selectByPrimaryKey(icId);
    }

    @Override
    public PageInfo<IssueComment> getIssueCommentByPage(IssueComment issueComment, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<IssueComment> issueComments = issueCommentMapper.selectAllByCondition(issueComment);
        PageInfo<IssueComment> issueCommentPageInfo = new PageInfo<>(issueComments);
        return issueCommentPageInfo;
    }

    @Override
    public int updateByIcIdSelective(IssueComment record) {
        return issueCommentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<IssueComment> getAllIssueComment(IssueComment issueComment) {
        return issueCommentMapper.selectAllByCondition(issueComment);
    }
}
