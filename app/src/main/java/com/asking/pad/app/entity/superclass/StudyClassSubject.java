package com.asking.pad.app.entity.superclass;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/5/24.
 */

public class StudyClassSubject {

    @JSONField(name="subjectCatalog")
    private String subjectCatalog;

    @JSONField(name="subjectCatalogName")
    private String subjectCatalogName;

    @JSONField(name="purchased")
    private String purchased;

    @JSONField(name="purchasedMsg")
    private String purchasedMsg;

    private List<StudyClassVersion> children = new ArrayList<>();

    public String getSubjectCatalog() {
        return subjectCatalog;
    }

    public void setSubjectCatalog(String subjectCatalog) {
        this.subjectCatalog = subjectCatalog;
    }

    public String getSubjectCatalogName() {
        return subjectCatalogName;
    }

    public void setSubjectCatalogName(String subjectCatalogName) {
        this.subjectCatalogName = subjectCatalogName;
    }

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }

    public String getPurchasedMsg() {
        return purchasedMsg;
    }

    public void setPurchasedMsg(String purchasedMsg) {
        this.purchasedMsg = purchasedMsg;
    }

    public List<StudyClassVersion> getChildren() {
        return children;
    }

    public void setChildren(List<StudyClassVersion> children) {
        this.children = children;
    }
}
