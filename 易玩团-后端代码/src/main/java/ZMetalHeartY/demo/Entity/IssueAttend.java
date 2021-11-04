package ZMetalHeartY.demo.Entity;

public class IssueAttend {
    private Integer iaId;

    private Integer fuId;

    private Integer iId;

    private Integer iaStatus;

    private String iaMsg;

    private FrontUser frontUser;

    private Issue issue;

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Integer getIaId() {
        return iaId;
    }

    public void setIaId(Integer iaId) {
        this.iaId = iaId;
    }

    public Integer getFuId() {
        return fuId;
    }

    public void setFuId(Integer fuId) {
        this.fuId = fuId;
    }

    public Integer getiId() {
        return iId;
    }

    public void setiId(Integer iId) {
        this.iId = iId;
    }

    public Integer getIaStatus() {
        return iaStatus;
    }

    public void setIaStatus(Integer iaStatus) {
        this.iaStatus = iaStatus;
    }

    public String getIaMsg() {
        return iaMsg;
    }

    public void setIaMsg(String iaMsg) {
        this.iaMsg = iaMsg == null ? null : iaMsg.trim();
    }

    public FrontUser getFrontUser() {
        return frontUser;
    }

    public void setFrontUser(FrontUser frontUser) {
        this.frontUser = frontUser;
    }
}