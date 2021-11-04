package ZMetalHeartY.demo.Mapper;


import ZMetalHeartY.demo.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper{

    int register(User user);

    int exist(User user);

    int delete(int userid);

    int update(User user);

    void setRole(int userid, int rolesid);

    User getByOpenid(User user);

    User getByID(int userid);

    User getByUsername(String username);

    String getRolesByUserid(int userid);

    List<String> getAgid(int userid);

    List<User> findAll(User user);

    int count(User user);

    User findByAccount(@Param("account") String account);


}
