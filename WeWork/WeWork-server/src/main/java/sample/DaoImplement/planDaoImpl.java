package sample.DaoImplement;

import Core.annotation.Inject;
import com.alibaba.druid.pool.DruidDataSource;
import sample.Beans.plan;
import sample.Dao.planDao;
import sample.Utils.SqlUtil;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class planDaoImpl implements planDao {

    @Inject
    DruidDataSource d;

    @Override
    public List<Object> getPlan(int user_id) {

        Connection c=null;

        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return null;
        }
        String sql="select * from plan where user_id=?";


        return SqlUtil.select(c,sql,plan.class,user_id);
    }

    @Override
    public int getPlanId(plan p) {
        Connection c=null;

        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return -1;
        }
        String sql="select id from plan where user_id=? and plan_name=?";
        List<Object>plan=SqlUtil.select(c,sql,plan.class,p.getUser_id(),p.getPlan_name());
        plan res=(plan)plan.get(0);
        return res.getId();
    }

    @Override
    public boolean updatePlan(plan p) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }
        String sql="update plan set plan_text=? where id=?";
        return SqlUtil.executeUpdate(c,sql,p.getPlan_text(),p.getId())!=-1;
    }

    @Override
    public boolean insertPlan(plan p) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }
        String sql;
        if(p.getPlan_text()==null){
            sql="insert into plan values(null,?,?,?,?) on duplicate key update planLog_text=values(planLog_text)";
        }else if(p.getPlanLog_text()==null){
            sql="insert into plan values(null,?,?,?,?) on duplicate key update plan_text= values(plan_text)";
        }else{
            sql="insert into plan values(null,?,?,?,?) on duplicate key update plan_text= values(plan_text) , planLog_text=values(planLog_text)";
        }

        int res=SqlUtil.executeUpdate(c,sql,p.getUser_id(),p.getPlan_text(),p.getPlanLog_text(),p.getPlan_name());

        return res!=-1;
    }

    @Override
    public boolean deletePlan(int user_id, String plan_name) {
        Connection c=null;
        int res;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }

        String sql="delete from plan where user_id=? and plan_name=?";
        res=SqlUtil.executeUpdate(c,sql,user_id,plan_name);
        return res!=-1;
    }
}
