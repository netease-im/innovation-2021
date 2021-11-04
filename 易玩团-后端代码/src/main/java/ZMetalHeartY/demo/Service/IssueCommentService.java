package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.Issue;
import ZMetalHeartY.demo.Entity.IssueComment;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IssueCommentService {

    int deleteByIcId(Integer icId);

    int insert(IssueComment issueComment);

    IssueComment selectByIcId(Integer icId);

    PageInfo<IssueComment> getIssueCommentByPage(IssueComment issueComment, Integer page, Integer limit);

    int updateByIcIdSelective(IssueComment record);

    List<IssueComment> getAllIssueComment(IssueComment issueComment);
    
}
