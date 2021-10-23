package sample.Dao;

import sample.Beans.plan;

import java.util.List;

public interface planDao {
    List<Object> getPlan(int user_id);
    boolean insertPlan(plan p);
    int getPlanId(plan p);
    boolean deletePlan(int user_id,String plan_name);
    boolean updatePlan(plan p);
}
