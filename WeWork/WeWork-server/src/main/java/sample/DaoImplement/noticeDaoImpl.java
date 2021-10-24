package sample.DaoImplement;

import Core.annotation.Inject;
import com.alibaba.druid.pool.DruidDataSource;
import sample.Beans.notice;
import sample.Beans.noticeFull;
import sample.Dao.noticeDao;
import sample.Utils.SqlUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class noticeDaoImpl implements noticeDao {

    @Inject
    DruidDataSource d;

    public DruidDataSource getD() {
        return d;
    }

    public void setD(DruidDataSource d) {
        this.d = d;
    }

    @Override
    public boolean insertNotice(notice n) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }
        String sql="insert into notice values(null,?,?,?,?,?)";
        return SqlUtil.executeUpdate(c,sql,n.getAcceptor(),
                n.getSender(),n.getContent()
                ,n.getType(),n.getRead())!=-1;
    }

    @Override
    public List<Object> getNotices(int acceptor) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return null;
        }
        String sql="select * from noticefull where acceptor=?";
        return SqlUtil.select(c,sql, noticefull.class,acceptor);
    }

    @Override
    public boolean deleteNotice(int id) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }
        String sql="delete from notice where id=?";
        return SqlUtil.executeUpdate(c,sql,id)!=-1;
    }

    @Override
    public boolean updateNoticeContent(notice n) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }

        String sql="update notice set content=? where id=?";
        return SqlUtil.executeUpdate(c,sql,n.getContent(),n.getId())!=-1;
    }

    @Override
    public boolean updateNoticeRead(notice n) {
        Connection c=null;
        try {
            c=d.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(c==null){
            return false;
        }

        String sql="update notice set `read`=? where id=?";
        return SqlUtil.executeUpdate(c,sql,n.getRead(),n.getId())!=-1;
    }
}
