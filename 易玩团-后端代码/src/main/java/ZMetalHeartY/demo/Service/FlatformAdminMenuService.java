package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.FlatformAdminMenu;

import java.util.List;


public interface FlatformAdminMenuService {

    List<FlatformAdminMenu> findByUid(Integer uid);

    List<FlatformAdminMenu> findAll();

    List<FlatformAdminMenu> findAllPage(FlatformAdminMenu flatformAdminMenu);

    int count(FlatformAdminMenu flatformAdminMenu);

    FlatformAdminMenu findById(Integer id);

    int insert(FlatformAdminMenu flatformAdminMenu);

    int update(FlatformAdminMenu flatformAdminMenu);

    int delete(FlatformAdminMenu flatformAdminMenu);

    List<FlatformAdminMenu> findByPage(FlatformAdminMenu flatformAdminMenu, int currentPage, int pageSize);

    FlatformAdminMenu findByPid(Integer pid);

    List<FlatformAdminMenu> findByMenu(Integer value);

    List<FlatformAdminMenu> findByType(String type);
}
