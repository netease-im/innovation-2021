package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.IssueAttend;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IssueAttendService {

    int deleteByIaId(Integer iaId);

    int insert(IssueAttend issueAttend);

    IssueAttend selectByIaId(Integer iaId);

    PageInfo<IssueAttend> getIssueAttendByPage(IssueAttend issueAttend, Integer page, Integer limit);

    int updateByIaIdSelective(IssueAttend record);

    List<IssueAttend> getAllIssueAttend(IssueAttend issueAttend);

    int adopt(Integer iaId);

    int refuse(Integer iaId);
}
