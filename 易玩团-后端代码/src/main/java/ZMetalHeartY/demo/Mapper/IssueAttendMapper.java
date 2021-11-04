package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.IssueAttend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IssueAttendMapper {
    int deleteByPrimaryKey(Integer iaId);

    int insert(IssueAttend record);

    int insertSelective(IssueAttend record);

    boolean isAttend(@Param("fuId") Integer fuId,@Param("iId") Integer iId);

    IssueAttend selectByPrimaryKey(Integer iaId);

    List<IssueAttend> selectByIId(Integer iId);

    List<IssueAttend> selectAllByCondition(IssueAttend record);

    int updateByPrimaryKeySelective(IssueAttend record);

    int updateByPrimaryKey(IssueAttend record);

}