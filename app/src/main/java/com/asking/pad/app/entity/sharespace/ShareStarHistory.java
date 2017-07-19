package com.asking.pad.app.entity.sharespace;

/**
 * Created by linbin on 2017/7/18.
 */

public class ShareStarHistory {

    /**
     * id : 596dc8e869bfa60925c7be00
     * userId : 596474a969bfa603b73843a6
     * nickName : 大概如此了
     * mobile : 15005936792
     * createTime : 1500307200000
     * sort : 5
     * weekOfYear : 29
     * adoptNum : 11
     * answerNum : 11
     * year : 2017
     */

    private String id;
    private String userId;
    private String nickName;
    private String mobile;
    private long createTime;
    private int sort;
    private int weekOfYear;
    private long adoptNum;
    private long answerNum;
    private int year;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public long getAdoptNum() {
        return adoptNum;
    }

    public void setAdoptNum(long adoptNum) {
        this.adoptNum = adoptNum;
    }

    public long getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(long answerNum) {
        this.answerNum = answerNum;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
