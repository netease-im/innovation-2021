package ZMetalHeartY.demo.Entity;

public class IssueComment {
    private Integer icId;

    private Integer fuId;

    private Integer iId;

    private String icMsg;

    private FrontUser frontUser;

    public Integer getIcId() {
        return icId;
    }

    public void setIcId(Integer icId) {
        this.icId = icId;
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

    public String getIcMsg() {
        return icMsg;
    }

    public void setIcMsg(String icMsg) {
        this.icMsg = icMsg == null ? null : icMsg.trim();
    }

    public FrontUser getFrontUser() {
        return frontUser;
    }

    public void setFrontUser(FrontUser frontUser) {
        this.frontUser = frontUser;
    }
}