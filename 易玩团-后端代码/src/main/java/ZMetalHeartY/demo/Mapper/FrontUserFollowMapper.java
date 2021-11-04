package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.FrontUserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FrontUserFollowMapper {
    int deleteByPrimaryKey(Integer fuId, Integer fufFuId);

    int insert(FrontUserFollow record);

    int insertSelective(FrontUserFollow record);

    FrontUserFollow selectByPrimaryKey(Integer fufId);

    List<FrontUserFollow> selectAllByCondition(FrontUserFollow frontUserFollow);

    int updateByPrimaryKeySelective(FrontUserFollow record);

    int updateByPrimaryKey(FrontUserFollow record);
}