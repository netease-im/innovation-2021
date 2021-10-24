package sample.Dao;

import sample.Beans.user;

public interface userDao {
    user getUser(String email,String password);
    user getUser(String email);
    boolean insertUser(user u);
    user getUserEmail(int user_id);
    boolean updateUser(user u);
    boolean updateUserContacts(user u);
}
