package com.asking.pad.app.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wxwang on 2017/7/18.
 */

public class QuestionEntity {

    private String id;
    private String userId;
    private String userName;
    private String title;
    private String description;
    private String km;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCaifu() {
        return caifu;
    }

    public void setCaifu(int caifu) {
        this.caifu = caifu;
    }


    private String levelId;

    public String getCreateDate_Fmt() {
        return createDate_Fmt;
    }

    public void setCreateDate_Fmt(String createDate_Fmt) {
        this.createDate_Fmt = createDate_Fmt;
    }

    public int getAnswer_size() {
        return answer_size;
    }

    public void setAnswer_size(int answer_size) {
        this.answer_size = answer_size;
    }

    private String createDate_Fmt;
    private String state;
    private int caifu;

    private int answer_size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}