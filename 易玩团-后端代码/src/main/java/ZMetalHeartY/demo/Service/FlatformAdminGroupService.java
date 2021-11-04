package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.FlatformAdminGroup;

import java.util.List;


public interface FlatformAdminGroupService {

    FlatformAdminGroup findByUid(Integer uid);

    int insert(FlatformAdminGroup flatformAdminGroup);

    int update(FlatformAdminGroup flatformAdminGroup);

    int delete(FlatformAdminGroup flatformAdminGroup);

    FlatformAdminGroup findById(Integer id);

    List<FlatformAdminGroup> findByPage(FlatformAdminGroup flatformAdminGroup, int currentPage, int pageSize);

    List<FlatformAdminGroup> findAll(FlatformAdminGroup flatformAdminGroup);

    int count(FlatformAdminGroup flatformAdminGroup);
}
