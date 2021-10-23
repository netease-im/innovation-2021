package sample.SingletonHandler;

import Core.annotation.Inject;
import Core.annotation.Singleton;
import Core.annotation.SingletonObjHandler;
import sample.DaoImplement.*;


@SingletonObjHandler
public class ImplSingletonHandler {

    @Singleton
    public userDaoImpl getUserDapImpl(){
        return new userDaoImpl();
    }

    @Singleton
    public planDaoImpl getPlanDaoImpl(){return new planDaoImpl();}

    @Singleton
    public goalDaoImpl getGoalDaoImpl(){
        return new goalDaoImpl();
    }

    @Singleton
    public noticeDaoImpl getNoticeDaoImpl(){
        return new noticeDaoImpl();
    }

    @Singleton
    public coworkDaoImpl getCoworkImpl(){
        return new coworkDaoImpl();
    }
}
