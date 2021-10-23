package sample.Service;

import Core.annotation.Inject;
import com.alibaba.fastjson.JSON;
import sample.Beans.notice;
import sample.DaoImplement.noticeDaoImpl;

import java.util.List;

public class noticeService {
    @Inject
    noticeDaoImpl noticeDao;

    public noticeDaoImpl getNoticeDao() {
        return noticeDao;
    }

    public void setNoticeDao(noticeDaoImpl noticeDao) {
        this.noticeDao = noticeDao;
    }

    public boolean addNotice(notice n){
        return noticeDao.insertNotice(n);
    }

    public String getNotice(int user_id){
        List<Object>list=noticeDao.getNotices(user_id);
        return JSON.toJSONString(list);
    }

    public boolean deleteNotice(int id){
        return noticeDao.deleteNotice(id);
    }

    public boolean updateNoticeContent(notice n){
        return noticeDao.updateNoticeContent(n);
    }

    public boolean updateNoticeRead(notice n){
        return noticeDao.updateNoticeRead(n);
    }
}
