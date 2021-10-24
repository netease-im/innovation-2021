package sample.Service;

import Core.annotation.Inject;
import sample.Beans.user;
import sample.DaoImplement.userDaoImpl;

public class UserService {
    @Inject
    userDaoImpl userDao;

    public boolean addUser(user u){
        return userDao.insertUser(u);
    }

    public boolean isUnique(user u){
        user check=userDao.getUser(u.getEmail());
        return check==null;
    }

    public user getUser(String email){
        return userDao.getUser(email);
    }

    public user checkUser(user u){
        return userDao.getUser(u.getEmail(),u.getPassword());
    }

    public boolean updateUser(user u){
        return userDao.updateUser(u);
    }

    public boolean updateUserContacts(user u){
        return userDao.updateUserContacts(u);
    }

    public user getUserEmail(int user_id){
        return userDao.getUserEmail(user_id);
    }
}
