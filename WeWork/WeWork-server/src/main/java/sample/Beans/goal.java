package sample.Beans;

public class goal {
    private int id;
    private int user_id;
    private String goal_name;
    private String goal;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getGoal_name() {
        return goal_name;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setGoal_name(String goal_name) {
        this.goal_name = goal_name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
