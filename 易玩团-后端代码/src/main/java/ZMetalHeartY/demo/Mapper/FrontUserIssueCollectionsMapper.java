package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.FrontUserIssueCollections;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FrontUserIssueCollectionsMapper {
    int deleteByPrimaryKey(@Param("fuId") Integer fuId,@Param("iId") Integer iId);

    int insert(FrontUserIssueCollections record);

    int insertSelective(FrontUserIssueCollections record);

    FrontUserIssueCollections selectByPrimaryKey(Integer fuicId);

    List<FrontUserIssueCollections> selectAllByCondition(FrontUserIssueCollections record);

    int updateByPrimaryKeySelective(FrontUserIssueCollections record);

    int updateByPrimaryKey(FrontUserIssueCollections record);
}