package sample.Service;

import Core.annotation.Inject;
import com.alibaba.fastjson.JSON;
import sample.Beans.plan;
import sample.DaoImplement.planDaoImpl;

import java.util.List;

public class planService {

    @Inject
    planDaoImpl planDao;

    public boolean insert(plan p){
        return planDao.insertPlan(p);
    }

    /**
     *
     * @param user_id identify the user
     * @return the json string of the planInfo
     */
    public String getPlan(int user_id){
        List<Object>list=planDao.getPlan(user_id);
        return JSON.toJSONString(list);
    }

    public int getPlanId(plan p){
        return planDao.getPlanId(p);
    }

    /**
     *
     * @param user_id identify the user
     * @param plan_name identify the plan
     * @return if the plan is deleted successfully
     */
    public boolean deletePlan(int user_id,String plan_name){
        return planDao.deletePlan(user_id,plan_name);
    }

    public boolean updatePlanById(plan p){
        return planDao.updatePlan(p);
    }

}
