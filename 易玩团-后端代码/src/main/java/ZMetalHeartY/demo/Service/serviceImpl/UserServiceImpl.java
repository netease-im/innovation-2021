package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Mapper.UserMapper;
import ZMetalHeartY.demo.Service.UserService;
import ZMetalHeartY.demo.Utils.PageBean;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> findByPage(User user, Integer currentPage, Integer pageSize) {
        if(currentPage==null){
            currentPage = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }
        PageHelper.startPage(currentPage,pageSize);//PageHelper使用
        PageHelper.orderBy("userid DESC");//分页排序
        List<User> userList = userMapper.findAll(user);       //全部记录列表
        int countNums = userMapper.count(user);         //总记录数
        PageBean<User> pageData = new PageBean<>(currentPage, pageSize, countNums);
        pageData.setItems(userList);
        return pageData.getItems();
    }

    @Override
    public User findByAccount(String account) {
        return userMapper.findByAccount(account);
    }


}
