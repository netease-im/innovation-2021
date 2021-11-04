package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.IssueComment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IssueCommentMapper {
    int deleteByPrimaryKey(Integer icId);

    int insert(IssueComment record);

    int insertSelective(IssueComment record);

    IssueComment selectByPrimaryKey(Integer icId);

    List<IssueComment> selectAllByCondition(IssueComment record);

    int updateByPrimaryKeySelective(IssueComment record);

    int updateByPrimaryKey(IssueComment record);
}