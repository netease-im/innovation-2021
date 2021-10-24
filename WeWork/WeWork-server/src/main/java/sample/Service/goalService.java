package sample.Service;

import Core.annotation.Inject;
import com.alibaba.fastjson.JSON;
import sample.Beans.goal;
import sample.DaoImplement.goalDaoImpl;

import java.util.List;

public class goalService {

    @Inject
    goalDaoImpl g;

    public goalDaoImpl getG() {
        return g;
    }

    public void setG(goalDaoImpl g) {
        this.g = g;
    }

    public boolean insert(goal goal){
        return g.insert(goal);
    }

    public String getGoal(int user_id){
        List<Object>list=g.getGoal(user_id);
        return JSON.toJSONString(list);
    }

    public boolean deleteGoal(int user_id,String goal_name){
        return g.deleteGoal(user_id,goal_name);
    }

}
