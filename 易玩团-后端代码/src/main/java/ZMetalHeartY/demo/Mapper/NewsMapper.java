package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NewsMapper {
    int deleteByPrimaryKey(Integer nId);

    int insert(News record);

    int insertSelective(News record);

    News selectByPrimaryKey(Integer nId);

    List<News> selectAllByCondition(News record);

    int updateByPrimaryKeySelective(News record);

    int updateByPrimaryKey(News record);
}