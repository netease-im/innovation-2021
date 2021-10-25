package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.FrontUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FrontUserMapper {
    int deleteByPrimaryKey(Integer fuId);

    int insert(FrontUser record);

    int insertSelective(FrontUser record);

    int exist(FrontUser record);

    FrontUser getByOpenid(String openid);



    FrontUser selectByPrimaryKey(Integer fuId);

    List<FrontUser> selectAllByCondition(FrontUser record);

    int updateByPrimaryKeySelective(FrontUser record);

    int updateByPrimaryKey(FrontUser record);
}