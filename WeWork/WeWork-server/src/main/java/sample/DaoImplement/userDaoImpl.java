package sample.DaoImplement;

import Core.annotation.Inject;
import com.alibaba.druid.pool.DruidDataSource;
import sample.Beans.user;
import sample.Dao.userDao;
import sample.Utils.SqlUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class userDaoImpl implements userDao {

    @Inject
    DruidDataSource d;


    public user getUserEmail(int user_id){
        String sql="select * from user where id=?";
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return null;
        }
        List<Object>users=SqlUtil.select(c,sql,user.class,user_id);
        if(users.size()==0){
            return null;
        }
        return (user)users.get(0);
    }

    @Override
    public boolean updateUser(user u) {
        String sql="update user set email=?,password=?,phone=?,user_name=? where id=?";
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }
        return SqlUtil.executeUpdate(c,sql,u.getEmail(),
                u.getPassword(),u.getPhone(),u.getUser_name(),
                u.getId())!=-1;
    }

    @Override
    public user getUser(String email, String password) {
        String sql="select * from user where email=? and password=?";
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(c==null){
            return null;
        }

        List<Object>users=SqlUtil.select(c,sql,user.class,email,password);
        if(users.size()==0){
            return null;
        }
        return (user)users.get(0);
    }
    @Override
    public user getUser(String email) {
        String sql="select * from user where email=?";
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(c==null){
            return null;
        }

        List<Object>users=SqlUtil.select(c,sql,user.class,email);
        if(users.size()==0){
            return null;
        }
        return (user)users.get(0);
    }

    @Override
    public boolean insertUser(user u) {
        String sql="insert into user values(null,?,?,?,?,?)";
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }

        boolean res=SqlUtil.executeUpdate(c,sql,u.getEmail(),
                u.getPassword(),u.getPhone(),
                u.getUser_name(),u.getContacts())!=-1;

        try {
            c.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean updateUserContacts(user u) {
        String sql="update user set contacts=? where id=?";

        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }

        return SqlUtil.executeUpdate(c,sql,u.getContacts(),u.getId())!=-1;
    }
}
