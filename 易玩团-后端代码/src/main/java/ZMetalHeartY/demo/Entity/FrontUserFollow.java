package ZMetalHeartY.demo.Entity;

import javax.persistence.criteria.From;

public class FrontUserFollow {
    private Integer fufId;

    private Integer fuId;

    private Integer fufFuId;

    private FrontUser frontUser;

    private FrontUser followedFrontUser;

    private Boolean isFollow;

    public Integer getFufId() {
        return fufId;
    }

    public void setFufId(Integer fufId) {
        this.fufId = fufId;
    }

    public Integer getFuId() {
        return fuId;
    }

    public void setFuId(Integer fuId) {
        this.fuId = fuId;
    }

    public Integer getFufFuId() {
        return fufFuId;
    }

    public void setFufFuId(Integer fufFuId) {
        this.fufFuId = fufFuId;
    }

    public FrontUser getFrontUser() {
        return frontUser;
    }

    public void setFrontUser(FrontUser frontUser) {
        this.frontUser = frontUser;
    }

    public FrontUser getFollowedFrontUser() {
        return followedFrontUser;
    }

    public void setFollowedFrontUser(FrontUser followedFrontUser) {
        this.followedFrontUser = followedFrontUser;
    }

    public Boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Boolean follow) {
        isFollow = follow;
    }
}