package ZMetalHeartY.demo.Service;


import ZMetalHeartY.demo.Entity.Issue;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZMetalHeartY
 */
@Service
public interface IssueService {

    int deleteByIId(Integer iId);

    int insert(Issue issue);

    Issue selectByIId(Integer iId);

    PageInfo<Issue> getIssueByPage(Issue issue, Integer page, Integer limit);

    int updateByIIdSelective(Issue record);

    List<Issue> getAllIssue(Issue issue);

    int adopt(Integer iId);

    int refuse(Integer iId);

    List<Issue> getGetListContent(List<Issue> issues, Integer fromFuId);

    Issue getGetIssueContent(Issue issue, Integer fromFuId);

}
