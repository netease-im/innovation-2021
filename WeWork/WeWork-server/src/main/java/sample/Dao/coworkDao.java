package sample.Dao;

import sample.Beans.cowork;

import java.util.List;

public interface coworkDao {
    boolean insert(cowork c);
    List<Object> getCowork(int user_id);
    boolean deleteCowork(int id);
}
