package ZMetalHeartY.demo.Entity;

import java.util.Date;

public class News {
    private Integer nId;

    private Integer iId;

    private Integer fromUserId;

    private Integer toUserId;

    private Integer toCommentId;

    private Integer toNewsId;

    private Date nCreateTime;

    private String nMsg;

    private Integer nType;

    private Integer nIsRead;

    private FrontUser frontUser;

    private FrontUser toFrontUser;

    private IssueComment issueComment;

    private Issue issue;


    public IssueComment getIssueComment() {
        return issueComment;
    }

    public void setIssueComment(IssueComment issueComment) {
        this.issueComment = issueComment;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public FrontUser getFrontUser() {
        return frontUser;
    }

    public void setFrontUser(FrontUser frontUser) {
        this.frontUser = frontUser;
    }

    public FrontUser getToFrontUser() {
        return toFrontUser;
    }

    public void setToFrontUser(FrontUser toFrontUser) {
        this.toFrontUser = toFrontUser;
    }

    public Integer getnId() {
        return nId;
    }

    public void setnId(Integer nId) {
        this.nId = nId;
    }

    public Integer getiId() {
        return iId;
    }

    public void setiId(Integer iId) {
        this.iId = iId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getToCommentId() {
        return toCommentId;
    }

    public void setToCommentId(Integer toCommentId) {
        this.toCommentId = toCommentId;
    }

    public Integer getToNewsId() {
        return toNewsId;
    }

    public void setToNewsId(Integer toNewsId) {
        this.toNewsId = toNewsId;
    }

    public Date getnCreateTime() {
        return nCreateTime;
    }

    public void setnCreateTime(Date nCreateTime) {
        this.nCreateTime = nCreateTime;
    }

    public String getnMsg() {
        return nMsg;
    }

    public void setnMsg(String nMsg) {
        this.nMsg = nMsg == null ? null : nMsg.trim();
    }

    public Integer getnType() {
        return nType;
    }

    public void setnType(Integer nType) {
        this.nType = nType;
    }

    public Integer getnIsRead() {
        return nIsRead;
    }

    public void setnIsRead(Integer nIsRead) {
        this.nIsRead = nIsRead;
    }
}