package ZMetalHeartY.demo.Entity;

import java.util.Date;

public class FrontUser {
    private Integer fuId;

    private String fuOpenid;

    private String fuNickname;

    private Integer fuSex;

    private String fuImg;

    private String fuPhone;

    private Integer fuCredit;

    private String fuNote;

    private String fuUsername;

    private String fuPassword;

    private Integer fuStatus;

    private Date fuCreateTime;



    public Integer getFuId() {
        return fuId;
    }

    public void setFuId(Integer fuId) {
        this.fuId = fuId;
    }

    public String getFuOpenid() {
        return fuOpenid;
    }

    public void setFuOpenid(String fuOpenid) {
        this.fuOpenid = fuOpenid == null ? null : fuOpenid.trim();
    }

    public String getFuNickname() {
        return fuNickname;
    }

    public void setFuNickname(String fuNickname) {
        this.fuNickname = fuNickname == null ? null : fuNickname.trim();
    }

    public Integer getFuSex() {
        return fuSex;
    }

    public void setFuSex(Integer fuSex) {
        this.fuSex = fuSex;
    }

    public String getFuImg() {
        return fuImg;
    }

    public void setFuImg(String fuImg) {
        this.fuImg = fuImg == null ? null : fuImg.trim();
    }

    public String getFuPhone() {
        return fuPhone;
    }

    public void setFuPhone(String fuPhone) {
        this.fuPhone = fuPhone == null ? null : fuPhone.trim();
    }

    public Integer getFuCredit() {
        return fuCredit;
    }

    public void setFuCredit(Integer fuCredit) {
        this.fuCredit = fuCredit;
    }

    public String getFuNote() {
        return fuNote;
    }

    public void setFuNote(String fuNote) {
        this.fuNote = fuNote == null ? null : fuNote.trim();
    }

    public String getFuUsername() {
        return fuUsername;
    }

    public void setFuUsername(String fuUsername) {
        this.fuUsername = fuUsername == null ? null : fuUsername.trim();
    }

    public String getFuPassword() {
        return fuPassword;
    }

    public void setFuPassword(String fuPassword) {
        this.fuPassword = fuPassword == null ? null : fuPassword.trim();
    }

    public Integer getFuStatus() {
        return fuStatus;
    }

    public void setFuStatus(Integer fuStatus) {
        this.fuStatus = fuStatus;
    }

    public Date getFuCreateTime() {
        return fuCreateTime;
    }

    public void setFuCreateTime(Date fuCreateTime) {
        this.fuCreateTime = fuCreateTime;
    }
}