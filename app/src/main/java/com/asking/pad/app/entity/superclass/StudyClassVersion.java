package com.asking.pad.app.entity.superclass;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/5/24.
 */

public class StudyClassVersion{
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
}
