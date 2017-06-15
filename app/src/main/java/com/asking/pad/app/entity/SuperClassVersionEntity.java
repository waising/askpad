package com.asking.pad.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jswang on 2017/4/11.
 */

public class SuperClassVersionEntity implements Parcelable{
    @JSONField(name="version_id")
    private int versionId;//": 16,
    private int sort;//": 1,
    @JSONField(name="version_name")
    private String versionName;//": "人教版",
    @JSONField(name="version_state")
    private int versionState;//": "1",
    @JSONField(name="subject_catalog_id")
    private int subjectCatalogId;//": 2,
    @JSONField(name="subject_catalog_name")
    private String subjectCatalogName;//": "数学",
    private int agency;//": "2",
    @JSONField(name="subject_catalog_code")
    private String subjectCatalogCode;//": "M",
    private int stage;//": "2"



    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionState() {
        return versionState;
    }

    public void setVersionState(int versionState) {
        this.versionState = versionState;
    }

    public int getSubjectCatalogId() {
        return subjectCatalogId;
    }

    public void setSubjectCatalogId(int subjectCatalogId) {
        this.subjectCatalogId = subjectCatalogId;
    }

    public String getSubjectCatalogName() {
        return subjectCatalogName;
    }

    public void setSubjectCatalogName(String subjectCatalogName) {
        this.subjectCatalogName = subjectCatalogName;
    }

    public int getAgency() {
        return agency;
    }

    public void setAgency(int agency) {
        this.agency = agency;
    }

    public String getSubjectCatalogCode() {
        return subjectCatalogCode;
    }

    public void setSubjectCatalogCode(String subjectCatalogCode) {
        this.subjectCatalogCode = subjectCatalogCode;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.versionId);
        dest.writeInt(this.sort);
        dest.writeString(this.versionName);
        dest.writeInt(this.versionState);
        dest.writeInt(this.subjectCatalogId);
        dest.writeString(this.subjectCatalogName);
        dest.writeInt(this.agency);
        dest.writeString(this.subjectCatalogCode);
        dest.writeInt(this.stage);
    }

    public SuperClassVersionEntity() {
    }

    public SuperClassVersionEntity(JSONObject o) {
        try {
            setVersionId(o.getInt("versionId"));
            setSort(o.getInt("sort"));
            setVersionName(o.getString("versionName"));
            setVersionState(o.getInt("versionState"));
            setSubjectCatalogId(o.getInt("subjectCatalogId"));
            setSubjectCatalogName(o.getString("subjectCatalogName"));
            setAgency(o.getInt("agency"));
            setSubjectCatalogCode(o.getString("subjectCatalogCode"));
            setStage(o.getInt("stage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected SuperClassVersionEntity(Parcel in) {
        this.versionId = in.readInt();
        this.sort = in.readInt();
        this.versionName = in.readString();
        this.versionState = in.readInt();
        this.subjectCatalogId = in.readInt();
        this.subjectCatalogName = in.readString();
        this.agency = in.readInt();
        this.subjectCatalogCode = in.readString();
        this.stage = in.readInt();
    }

    public static final Creator<SuperClassVersionEntity> CREATOR = new Creator<SuperClassVersionEntity>() {
        @Override
        public SuperClassVersionEntity createFromParcel(Parcel source) {
            return new SuperClassVersionEntity(source);
        }

        @Override
        public SuperClassVersionEntity[] newArray(int size) {
            return new SuperClassVersionEntity[size];
        }
    };
}
