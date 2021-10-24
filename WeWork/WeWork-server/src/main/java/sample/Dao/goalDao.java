package sample.Dao;

import sample.Beans.goal;

import java.util.List;

public interface goalDao {
     boolean insert(goal g);
     List<Object> getGoal(int user_id);
     boolean deleteGoal(int user_id,String goal_name);
}
