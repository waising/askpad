package com.asking.pad.app.entity.superclass;

import java.util.List;

public class SuperLessonTree {

    private String versionLeveTipId;

    private String tipId;

    private String tipCode;

    private String tipName;

    private String versionLevelId;

    private String state;

    private String showType;

    private String orderIndex;

    private String tipRealName;

    private String parentId;

    private String purchased;

    private String purchasedMsg;

    private List<SuperLessonTree> children;

    public String getVersionLeveTipId() {
        return versionLeveTipId;
    }

    public void setVersionLeveTipId(String versionLeveTipId) {
        this.versionLeveTipId = versionLeveTipId;
    }

    public String getTipId() {
        return tipId;
    }

    public void setTipId(String tipId) {
        this.tipId = tipId;
    }

    public String getTipCode() {
        return tipCode;
    }

    public void setTipCode(String tipCode) {
        this.tipCode = tipCode;
    }

    public String getTipName() {
        return tipName;
    }

    public void setTipName(String tipName) {
        this.tipName = tipName;
    }

    public String getVersionLevelId() {
        return versionLevelId;
    }

    public void setVersionLevelId(String versionLevelId) {
        this.versionLevelId = versionLevelId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getTipRealName() {
        return tipRealName;
    }

    public void setTipRealName(String tipRealName) {
        this.tipRealName = tipRealName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public List<SuperLessonTree> getChildren() {
        return children;
    }

    public void setChildren(List<SuperLessonTree> children) {
        this.children = children;
    }
}
