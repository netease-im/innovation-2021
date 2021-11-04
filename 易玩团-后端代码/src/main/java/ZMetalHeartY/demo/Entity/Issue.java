package ZMetalHeartY.demo.Entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class Issue {

    @JSONField(name = "id")
    private Integer iId;

    private Integer fuId;

    private Integer iType;

    private String iTitle;

    private String iAddress;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date iCreateTime;

    private Integer iSex;

    private String iResume;

    private String iDetails;

    private String iPositionStart;

    private String iPositionEnd;

    private String iImg;

    private String iVideo;

    private String iAudio;

    private Integer iStatus;

    private Integer iPeopleNumber;

    private FrontUser frontUser;

    private List<IssueComment> issueComments;

    private String iconPath;

    private boolean isFollow;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date iStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date iEndTime;

    private List<IssueAttend> issueAttend;

    private Integer width;

    private Integer height;

    private Double longitude;

    private Double latitude;

    private Boolean isCollection;

    private Boolean isLike;

    public Boolean getCollection() {
        return isCollection;
    }

    public void setCollection(Boolean collection) {
        isCollection = collection;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }

    public Integer getiId() {
        return iId;
    }

    public void setiId(Integer iId) {
        this.iId = iId;
    }

    public Integer getFuId() {
        return fuId;
    }

    public void setFuId(Integer fuId) {
        this.fuId = fuId;
    }

    public Integer getiType() {
        return iType;
    }

    public void setiType(Integer iType) {
        this.iType = iType;
    }

    public String getiTitle() {
        return iTitle;
    }

    public void setiTitle(String iTitle) {
        this.iTitle = iTitle == null ? null : iTitle.trim();
    }

    public String getiAddress() {
        return iAddress;
    }

    public void setiAddress(String iAddress) {
        this.iAddress = iAddress == null ? null : iAddress.trim();
    }

    public Date getiCreateTime() {
        return iCreateTime;
    }

    public void setiCreateTime(Date iCreateTime) {
        this.iCreateTime = iCreateTime;
    }

    public Integer getiSex() {
        return iSex;
    }

    public void setiSex(Integer iSex) {
        this.iSex = iSex;
    }

    public String getiResume() {
        return iResume;
    }

    public void setiResume(String iResume) {
        this.iResume = iResume == null ? null : iResume.trim();
    }

    public String getiDetails() {
        return iDetails;
    }

    public void setiDetails(String iDetails) {
        this.iDetails = iDetails == null ? null : iDetails.trim();
    }

    public String getiPositionStart() {
        return iPositionStart;
    }

    public void setiPositionStart(String iPositionStart) {
        this.iPositionStart = iPositionStart == null ? null : iPositionStart.trim();
    }

    public String getiPositionEnd() {
        return iPositionEnd;
    }

    public void setiPositionEnd(String iPositionEnd) {
        this.iPositionEnd = iPositionEnd == null ? null : iPositionEnd.trim();
    }

    public String getiImg() {
        return iImg;
    }

    public void setiImg(String iImg) {
        this.iImg = iImg == null ? null : iImg.trim();
    }

    public String getiVideo() {
        return iVideo;
    }

    public void setiVideo(String iVideo) {
        this.iVideo = iVideo == null ? null : iVideo.trim();
    }

    public String getiAudio() {
        return iAudio;
    }

    public void setiAudio(String iAudio) {
        this.iAudio = iAudio == null ? null : iAudio.trim();
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public Integer getiPeopleNumber() {
        return iPeopleNumber;
    }

    public void setiPeopleNumber(Integer iPeopleNumber) {
        this.iPeopleNumber = iPeopleNumber;
    }

    public FrontUser getFrontUser() {
        return frontUser;
    }

    public void setFrontUser(FrontUser frontUser) {
        this.frontUser = frontUser;
    }

    public List<IssueComment> getIssueComments() {
        return issueComments;
    }

    public void setIssueComments(List<IssueComment> issueComments) {
        this.issueComments = issueComments;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Date getiStartTime() {
        return iStartTime;
    }

    public void setiStartTime(Date iStartTime) {
        this.iStartTime = iStartTime;
    }

    public Date getiEndTime() {
        return iEndTime;
    }

    public void setiEndTime(Date iEndTime) {
        this.iEndTime = iEndTime;
    }

    public List<IssueAttend> getIssueAttend() {
        return issueAttend;
    }

    public void setIssueAttend(List<IssueAttend> issueAttend) {
        this.issueAttend = issueAttend;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}