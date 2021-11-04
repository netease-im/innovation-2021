package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.User;

import java.util.List;

public interface UserService {

    List<User> findByPage(User user, Integer currentPage, Integer pageSize);

    User findByAccount(String account);
}
