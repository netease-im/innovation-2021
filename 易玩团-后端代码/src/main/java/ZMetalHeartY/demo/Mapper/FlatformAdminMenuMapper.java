package ZMetalHeartY.demo.Mapper;

import ZMetalHeartY.demo.Entity.FlatformAdminMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FlatformAdminMenuMapper {

    FlatformAdminMenu findByUid(@Param("uid") Integer uid);

    List<FlatformAdminMenu> findAll();

    List<FlatformAdminMenu> findAllPage(FlatformAdminMenu mainMenu);

    int count(FlatformAdminMenu mainMenu);

    int insert(FlatformAdminMenu mainMenu);

    int update(FlatformAdminMenu mainMenu);

    int delete(FlatformAdminMenu mainMenu);

    FlatformAdminMenu findById(@Param("id") Integer id);

    List<FlatformAdminMenu> findByAuLevel(@Param("value") Integer value);

    List<FlatformAdminMenu> findByType(String type);

}
