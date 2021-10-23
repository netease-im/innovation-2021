package sample.DaoImplement;

import Core.annotation.Inject;
import com.alibaba.druid.pool.DruidDataSource;
import sample.Beans.cowork;
import sample.Beans.coworkfull;
import sample.Dao.coworkDao;
import sample.Utils.SqlUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class coworkDaoImpl implements coworkDao {

    @Inject
    DruidDataSource d;

    public DruidDataSource getD() {
        return d;
    }

    public void setD(DruidDataSource d) {
        this.d = d;
    }

    @Override
    public boolean insert(cowork cowork) {
        Connection c=null;

        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }

        String sql="insert into cowork values (null,?,?)";

        return SqlUtil.executeUpdate(c,sql,cowork.getUser_id()
                ,cowork.getPlan_id())!=-1;
    }

    @Override
    public List<Object> getCowork(int partner) {
        Connection c=null;

        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return null;
        }

        String sql="select * from coworkfull where partner=?";

        return SqlUtil.select(c,sql, coworkfull.class,partner);
    }

    @Override
    public boolean deleteCowork(int id) {
        Connection c=null;

        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }

        String sql="delete from cowork where id=?";

        return SqlUtil.executeUpdate(c,sql,id)!=-1;
    }
}
