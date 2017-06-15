package com.asking.pad.app.entity.superclass;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jswang on 2017/5/24.
 */

public class StudyClassGrade implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.versionLevelId);
        dest.writeString(this.versionId);
        dest.writeString(this.levelId);
        dest.writeString(this.levelName);
        dest.writeString(this.textbook);
        dest.writeString(this.levelCode);
        dest.writeString(this.state);
        dest.writeString(this.type);
        dest.writeValue(this.isSelect);
    }

    public StudyClassGrade() {
    }

    protected StudyClassGrade(Parcel in) {
        this.versionLevelId = in.readString();
        this.versionId = in.readString();
        this.levelId = in.readString();
        this.levelName = in.readString();
        this.textbook = in.readString();
        this.levelCode = in.readString();
        this.state = in.readString();
        this.type = in.readString();
        this.isSelect = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<StudyClassGrade> CREATOR = new Parcelable.Creator<StudyClassGrade>() {
        @Override
        public StudyClassGrade createFromParcel(Parcel source) {
            return new StudyClassGrade(source);
        }

        @Override
        public StudyClassGrade[] newArray(int size) {
            return new StudyClassGrade[size];
        }
    };
}
