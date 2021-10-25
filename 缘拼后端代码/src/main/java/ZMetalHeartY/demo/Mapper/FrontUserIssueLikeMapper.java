package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.FrontUserIssueLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FrontUserIssueLikeMapper {
    int deleteByPrimaryKey(@Param("fuId") Integer fuId, @Param("iId") Integer iId);

    int insert(FrontUserIssueLike record);

    int insertSelective(FrontUserIssueLike record);

    FrontUserIssueLike selectByPrimaryKey(Integer fuilId);

    List<FrontUserIssueLike> selectAllByCondition(FrontUserIssueLike record);

    int updateByPrimaryKeySelective(FrontUserIssueLike record);

    int updateByPrimaryKey(FrontUserIssueLike record);
}