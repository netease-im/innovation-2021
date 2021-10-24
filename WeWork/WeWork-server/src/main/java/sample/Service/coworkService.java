package sample.Service;

import Core.annotation.Inject;
import com.alibaba.fastjson.JSON;
import sample.Beans.cowork;
import sample.DaoImplement.coworkDaoImpl;

import java.util.List;


public class coworkService {

    @Inject
    coworkDaoImpl coworkDao;

    public boolean addCowork(cowork c){
        return coworkDao.insert(c);
    }

    public String getCowork(int partner){
        List<Object> list=coworkDao.getCowork(partner);
        return JSON.toJSONString(list);
    }

    public boolean deleteCowork(int id){
        return coworkDao.deleteCowork(id);
    }

}
