package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.FlatformAdminGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FlatformAdminGroupMapper {

    FlatformAdminGroup findByUid(@Param("uid") Integer uid);

    List<FlatformAdminGroup> findAll(FlatformAdminGroup flatformAdminGroup);

    int count(FlatformAdminGroup flatformAdminGroup);

    int insert(FlatformAdminGroup flatformAdminGroup);

    int update(FlatformAdminGroup flatformAdminGroup);

    int delete(FlatformAdminGroup flatformAdminGroup);

    FlatformAdminGroup findById(Integer id);


}
