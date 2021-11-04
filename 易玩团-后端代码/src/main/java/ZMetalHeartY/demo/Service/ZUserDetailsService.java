package ZMetalHeartY.demo.Service;

import ZMetalHeartY.demo.Entity.User;
import ZMetalHeartY.demo.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ZUserDetailsService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user = userMapper.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("数据库中无此用户！");
        }

        StringBuffer roles = new StringBuffer();
        String role= userMapper.getRolesByUserid(user.getUserid());

        user.setRole(role);

        return user;

    }
}
