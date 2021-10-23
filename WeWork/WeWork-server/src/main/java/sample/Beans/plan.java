package sample.Beans;

public class plan {
    private int id;
    private int user_id;
    private String plan_text;
    private String planLog_text;
    private String plan_name;

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPlan_text() {
        return plan_text;
    }

    public String getPlanLog_text() {
        return planLog_text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setPlan_text(String plan_text) {
        this.plan_text = plan_text;
    }

    public void setPlanLog_text(String planLog_text) {
        this.planLog_text = planLog_text;
    }
}
