package sample.DaoImplement;

import Core.annotation.Inject;
import com.alibaba.druid.pool.DruidDataSource;
import sample.Beans.goal;
import sample.Dao.goalDao;
import sample.Utils.SqlUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class goalDaoImpl implements goalDao {

    @Inject
    DruidDataSource d;

    public DruidDataSource getD() {
        return d;
    }

    public void setD(DruidDataSource d) {
        this.d = d;
    }

    @Override
    public boolean insert(goal g) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }
        String sql= "insert into goal values(null,?,?,?) on duplicate key update goal=values(goal)";
        int res=SqlUtil.executeUpdate(c,sql,g.getUser_id(),
                g.getGoal_name(),g.getGoal());
        return res!=-1;
    }

    @Override
    public List<Object> getGoal(int user_id) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return null;
        }
        String sql="select * from goal where user_id=?";
        return SqlUtil.select(c,sql,goal.class,user_id);
    }

    @Override
    public boolean deleteGoal(int user_id, String goal_name) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }
        String sql="delete from goal where user_id=? and goal_name=?";
        return SqlUtil.executeUpdate(c,sql,user_id,goal_name)!=-1;
    }
}
