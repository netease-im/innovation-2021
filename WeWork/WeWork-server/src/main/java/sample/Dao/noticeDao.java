package sample.Dao;

import sample.Beans.notice;

import java.util.List;

public interface noticeDao {
    boolean insertNotice(notice n);
    List<Object>getNotices(int acceptor);
    boolean deleteNotice(int id);
    boolean updateNoticeContent(notice n);
    boolean updateNoticeRead(notice n);
}
