package com.asking.pad.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jswang on 2017/4/11.
 */

public class SuperClassGradeEntity implements Parcelable{
    @JSONField(name="version_id")
    private int versionId;//": 6,
    @JSONField(name="version_level_id")
    private int versionLevelId;//": 11,
    private String textbook;//": "必修1",
    @JSONField(name="level_code")
    private String levelCode;//": "H1",
    @JSONField(name="level_id")
    private int levelId;//": 11,

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    @JSONField(name="level_name")
    private String levelName;//": "高一",
    private int state;//": 1

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;//=2

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public int getVersionLevelId() {
        return versionLevelId;
    }

    public void setVersionLevelId(int versionLevelId) {
        this.versionLevelId = versionLevelId;
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

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public SuperClassGradeEntity() {
    }
    public SuperClassGradeEntity(JSONObject o) {
        try {
            setVersionId(o.getInt("versionId"));
            setVersionLevelId(o.getInt("versionLevelId"));
            setTextbook(o.getString("textbook"));
            setLevelCode(o.getString("levelCode"));
            setLevelId(o.getInt("levelId"));
            setLevelName(o.getString("levelName"));
            setState(o.getInt("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.versionId);
        dest.writeInt(this.versionLevelId);
        dest.writeString(this.textbook);
        dest.writeString(this.levelCode);
        dest.writeInt(this.levelId);
        dest.writeString(this.levelName);
        dest.writeInt(this.state);
        dest.writeString(this.type);
    }

    protected SuperClassGradeEntity(Parcel in) {
        this.versionId = in.readInt();
        this.versionLevelId = in.readInt();
        this.textbook = in.readString();
        this.levelCode = in.readString();
        this.levelId = in.readInt();
        this.levelName = in.readString();
        this.state = in.readInt();
        this.type = in.readString();
    }

    public static final Creator<SuperClassGradeEntity> CREATOR = new Creator<SuperClassGradeEntity>() {
        @Override
        public SuperClassGradeEntity createFromParcel(Parcel source) {
            return new SuperClassGradeEntity(source);
        }

        @Override
        public SuperClassGradeEntity[] newArray(int size) {
            return new SuperClassGradeEntity[size];
        }
    };
}
