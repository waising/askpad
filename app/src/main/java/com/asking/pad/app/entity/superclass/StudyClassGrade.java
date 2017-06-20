package com.asking.pad.app.entity.superclass;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jswang on 2017/5/24.
 */

public class StudyClassGrade{

    @JSONField(name = "version_level_id")
    private String versionLevelId;

    @JSONField(name = "version_id")
    private String versionId;

    @JSONField(name = "level_id")
    private String levelId;

    @JSONField(name = "level_name")
    private String levelName;

    @JSONField(name = "textbook")
    private String textbook;

    @JSONField(name = "level_code")
    private String levelCode;

    @JSONField(name = "state")
    private String state;

    @JSONField(name = "type")
    private String type;

    @JSONField(name="purchased")
    private String purchased;

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }

    public Boolean isSelect = false;

    public String getVersionLevelId() {
        return versionLevelId;
    }

    public void setVersionLevelId(String versionLevelId) {
        this.versionLevelId = versionLevelId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getTextbook() {
        return textbook;
    }

    public void setTextbook(String textbook) {
        this.textbook = textbook;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsBuy(){
        if(TextUtils.equals("0",getPurchased())){
            return  false;
        }
        return  true;
    }
}
