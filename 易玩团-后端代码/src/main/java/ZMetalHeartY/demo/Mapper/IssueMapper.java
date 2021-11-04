package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.Issue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IssueMapper {
    int deleteByPrimaryKey(Integer iId);

    int insert(Issue record);

    int insertSelective(Issue record);

    Issue selectByPrimaryKey(Integer iId);

    List<Issue> selectAllByCondition(Issue issue);

    int updateByPrimaryKeySelective(Issue record);

    int updateByPrimaryKey(Issue record);

    int adopt(Integer iId);

    int refuse(Integer iId);

    int complete(Integer iId);
}