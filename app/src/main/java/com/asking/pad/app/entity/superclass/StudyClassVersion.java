package com.asking.pad.app.entity.superclass;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/5/24.
 */

public class StudyClassVersion implements Parcelable {
    @JSONField(name="version_id")
    private String versionId;

    @JSONField(name="sort")
    private String sort;

    @JSONField(name="version_name")
    private String versionName;

    @JSONField(name="version_state")
    private String versionState;

    @JSONField(name="subject_catalog_id")
    private String subjectCatalogId;

    @JSONField(name="subject_catalog_name")
    private String subjectCatalogName;

    @JSONField(name="agency")
    private String agency;

    @JSONField(name="subject_catalog_code")
    private String subjectCatalogCode;

    @JSONField(name="stage")
    private String stage;

    public Boolean isSelect = false;

    public int dataType = 0;

    public StudyClassVersion(){}

    public StudyClassVersion(String versionName){
        this.versionName = versionName;
        this.dataType = 1;
        this.isSelect = false;
    }


    private List<StudyClassGrade> children = new ArrayList<>();

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionState() {
        return versionState;
    }

    public void setVersionState(String versionState) {
        this.versionState = versionState;
    }

    public String getSubjectCatalogId() {
        return subjectCatalogId;
    }

    public void setSubjectCatalogId(String subjectCatalogId) {
        this.subjectCatalogId = subjectCatalogId;
    }

    public String getSubjectCatalogName() {
        return subjectCatalogName;
    }

    public void setSubjectCatalogName(String subjectCatalogName) {
        this.subjectCatalogName = subjectCatalogName;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getSubjectCatalogCode() {
        return subjectCatalogCode;
    }

    public void setSubjectCatalogCode(String subjectCatalogCode) {
        this.subjectCatalogCode = subjectCatalogCode;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public List<StudyClassGrade> getChildren() {
        return children;
    }

    public void setChildren(List<StudyClassGrade> children) {
        this.children = children;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.versionId);
        dest.writeString(this.sort);
        dest.writeString(this.versionName);
        dest.writeString(this.versionState);
        dest.writeString(this.subjectCatalogId);
        dest.writeString(this.subjectCatalogName);
        dest.writeString(this.agency);
        dest.writeString(this.subjectCatalogCode);
        dest.writeString(this.stage);
        dest.writeValue(this.isSelect);
        dest.writeInt(this.dataType);
        dest.writeList(this.children);
    }

    protected StudyClassVersion(Parcel in) {
        this.versionId = in.readString();
        this.sort = in.readString();
        this.versionName = in.readString();
        this.versionState = in.readString();
        this.subjectCatalogId = in.readString();
        this.subjectCatalogName = in.readString();
        this.agency = in.readString();
        this.subjectCatalogCode = in.readString();
        this.stage = in.readString();
        this.isSelect = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.dataType = in.readInt();
        this.children = new ArrayList<StudyClassGrade>();
        in.readList(this.children, StudyClassGrade.class.getClassLoader());
    }

    public static final Parcelable.Creator<StudyClassVersion> CREATOR = new Parcelable.Creator<StudyClassVersion>() {
        @Override
        public StudyClassVersion createFromParcel(Parcel source) {
            return new StudyClassVersion(source);
        }

        @Override
        public StudyClassVersion[] newArray(int size) {
            return new StudyClassVersion[size];
        }
    };
}
