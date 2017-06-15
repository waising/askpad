package com.asking.pad.app.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wxiao on 2016/12/1.
 */

public class SuperSuperClassSpeakerEntity implements Serializable{

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        if(list==null){
            list = new ArrayList<>();
        }
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        @SerializedName("kind_name")
        private String kindName;
        @SerializedName("kind_id")
        private String kindId;
        @SerializedName("connect_html")
        private String connectHtml;
        @SerializedName("tipName")
        private String tipName;
        private SubjectBean subject;
        private String subjectList;
        private String reviewSize;
        private String type;
        private List<TabListBean> tabList = new ArrayList<>();

        public String getKindName() {
            return kindName;
        }

        public void setKindName(String kindName) {
            this.kindName = kindName;
        }

        public String getKindId() {
            return kindId;
        }

        public void setKindId(String kindId) {
            this.kindId = kindId;
        }

        public String getConnectHtml() {
            return connectHtml;
        }

        public void setConnectHtml(String connectHtml) {
            this.connectHtml = connectHtml;
        }

        public String getTipName() {
            return tipName;
        }

        public void setTipName(String tipName) {
            this.tipName = tipName;
        }

        public SubjectBean getSubject() {
            return subject;
        }

        public void setSubject(SubjectBean subject) {
            this.subject = subject;
        }

        public String getSubjectList() {
            return subjectList;
        }

        public void setSubjectList(String subjectList) {
            this.subjectList = subjectList;
        }

        public String getReviewSize() {
            return reviewSize;
        }

        public void setReviewSize(String reviewSize) {
            this.reviewSize = reviewSize;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<TabListBean> getTabList() {
            return tabList;
        }

        public void setTabList(List<TabListBean> tabList) {
            this.tabList = tabList;
        }

        public static class SubjectBean implements Serializable{

            private String id;
            @SerializedName("subject_description")
            private String subjectDescription;
            @SerializedName("subject_description_html")
            private String subjectDescriptionHtml;
            private String subjectKind;
            @SerializedName("tab_ids")
            private String tabIds;
            @SerializedName("options_num")
            private String optionsNum;
            private List<OptionsBean> options;
            @SerializedName("right_answer")
            private String rightAnswer;
            @SerializedName("refer_subject_id")
            private String referSubjectId;
            @SerializedName("gkt_state")
            private boolean gktState;
            @SerializedName("zt_state")
            private boolean ztState;
            @SerializedName("zt_size")
            private int ztSize;
            @SerializedName("user_answer")
            private String userAnswer;
            private String right;
            private String errorAnswer;
            @SerializedName("autopost_size")
            private int autopostSize;
            @SerializedName("answer_size")
            private int answerSize;
            @SerializedName("answer_right_size")
            private int answerRightSize;
            @SerializedName("have_child")
            private String haveChild;
            @SerializedName("refer_subject_title")
            private String referSubjectTitle;
            @SerializedName("subject_type")
            private SubjectTypeBean subjectTypeBean;
            private int difficulty;
            @SerializedName("mistake_rate")
            private String mistakeRate;
            @SerializedName("subject_code")
            private String subjectCode;
            @SerializedName("column_num")
            private int columnNum;
            @SerializedName("state_name")
            private String stateName;
            private String createDate;
            @SerializedName("details_answer")
            private String detailsAnswer;
            @SerializedName("details_answer_html")
            private String detailsAnswerHtml;
            private String createDateStr;
            private String state;
            @SerializedName("create_staff_id")
            private String createStaffId;
            @SerializedName("approval_staff_id")
            private String approvalStaffId;
            @SerializedName("subject_catalog_name")
            private String subjectCatalogName;
            @SerializedName("subject_catalog_code")
            private String subjectCatalogCode;
            private String random;
            @SerializedName("create_staff_name")
            private String createStaffName;
            private List<TipsBean> tips;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSubjectDescription() {
                return subjectDescription;
            }

            public void setSubjectDescription(String subjectDescription) {
                this.subjectDescription = subjectDescription;
            }

            public String getSubjectDescriptionHtml() {
                return subjectDescriptionHtml;
            }

            public void setSubjectDescriptionHtml(String subjectDescriptionHtml) {
                this.subjectDescriptionHtml = subjectDescriptionHtml;
            }

            public String getSubjectKind() {
                return subjectKind;
            }

            public void setSubjectKind(String subjectKind) {
                this.subjectKind = subjectKind;
            }

            public String getTabIds() {
                return tabIds;
            }

            public void setTabIds(String tabIds) {
                this.tabIds = tabIds;
            }

            public String getOptionsNum() {
                return optionsNum;
            }

            public void setOptionsNum(String optionsNum) {
                this.optionsNum = optionsNum;
            }

            public List<OptionsBean> getOptions() {
                return options;
            }

            public void setOptions(List<OptionsBean> options) {
                this.options = options;
            }

            public String getRightAnswer() {
                return rightAnswer;
            }

            public void setRightAnswer(String rightAnswer) {
                this.rightAnswer = rightAnswer;
            }

            public String getReferSubjectId() {
                return referSubjectId;
            }

            public void setReferSubjectId(String referSubjectId) {
                this.referSubjectId = referSubjectId;
            }

            public boolean isGktState() {
                return gktState;
            }

            public void setGktState(boolean gktState) {
                this.gktState = gktState;
            }

            public boolean isZtState() {
                return ztState;
            }

            public void setZtState(boolean ztState) {
                this.ztState = ztState;
            }

            public int getZtSize() {
                return ztSize;
            }

            public void setZtSize(int ztSize) {
                this.ztSize = ztSize;
            }

            public String getUserAnswer() {
                return userAnswer;
            }

            public void setUserAnswer(String userAnswer) {
                this.userAnswer = userAnswer;
            }

            public String getRight() {
                return right;
            }

            public void setRight(String right) {
                this.right = right;
            }

            public String getErrorAnswer() {
                return errorAnswer;
            }

            public void setErrorAnswer(String errorAnswer) {
                this.errorAnswer = errorAnswer;
            }

            public int getAutopostSize() {
                return autopostSize;
            }

            public void setAutopostSize(int autopostSize) {
                this.autopostSize = autopostSize;
            }

            public int getAnswerSize() {
                return answerSize;
            }

            public void setAnswerSize(int answerSize) {
                this.answerSize = answerSize;
            }

            public int getAnswerRightSize() {
                return answerRightSize;
            }

            public void setAnswerRightSize(int answerRightSize) {
                this.answerRightSize = answerRightSize;
            }

            public String getHaveChild() {
                return haveChild;
            }

            public void setHaveChild(String haveChild) {
                this.haveChild = haveChild;
            }

            public String getReferSubjectTitle() {
                return referSubjectTitle;
            }

            public void setReferSubjectTitle(String referSubjectTitle) {
                this.referSubjectTitle = referSubjectTitle;
            }

            public SubjectTypeBean getSubjectTypeBean() {
                return subjectTypeBean;
            }

            public void setSubjectTypeBean(SubjectTypeBean subjectTypeBean) {
                this.subjectTypeBean = subjectTypeBean;
            }

            public int getDifficulty() {
                return difficulty;
            }

            public void setDifficulty(int difficulty) {
                this.difficulty = difficulty;
            }

            public Object getMistakeRate() {
                return mistakeRate;
            }

            public void setMistakeRate(String mistakeRate) {
                this.mistakeRate = mistakeRate;
            }

            public String getSubjectCode() {
                return subjectCode;
            }

            public void setSubjectCode(String subjectCode) {
                this.subjectCode = subjectCode;
            }

            public int getColumnNum() {
                return columnNum;
            }

            public void setColumnNum(int columnNum) {
                this.columnNum = columnNum;
            }

            public String getStateName() {
                return stateName;
            }

            public void setStateName(String stateName) {
                this.stateName = stateName;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getDetailsAnswer() {
                return detailsAnswer;
            }

            public void setDetailsAnswer(String detailsAnswer) {
                this.detailsAnswer = detailsAnswer;
            }

            public String getDetailsAnswerHtml() {
                return detailsAnswerHtml;
            }

            public void setDetailsAnswerHtml(String detailsAnswerHtml) {
                this.detailsAnswerHtml = detailsAnswerHtml;
            }

            public String getCreateDateStr() {
                return createDateStr;
            }

            public void setCreateDateStr(String createDateStr) {
                this.createDateStr = createDateStr;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCreateStaffId() {
                return createStaffId;
            }

            public void setCreateStaffId(String createStaffId) {
                this.createStaffId = createStaffId;
            }

            public String getApprovalStaffId() {
                return approvalStaffId;
            }

            public void setApprovalStaffId(String approvalStaffId) {
                this.approvalStaffId = approvalStaffId;
            }

            public String getSubjectCatalogName() {
                return subjectCatalogName;
            }

            public void setSubjectCatalogName(String subjectCatalogName) {
                this.subjectCatalogName = subjectCatalogName;
            }

            public String getSubjectCatalogCode() {
                return subjectCatalogCode;
            }

            public void setSubjectCatalogCode(String subjectCatalogCode) {
                this.subjectCatalogCode = subjectCatalogCode;
            }

            public String getRandom() {
                return random;
            }

            public void setRandom(String random) {
                this.random = random;
            }

            public String getCreateStaffName() {
                return createStaffName;
            }

            public void setCreateStaffName(String createStaffName) {
                this.createStaffName = createStaffName;
            }

            public List<TipsBean> getTips() {
                return tips;
            }

            public void setTips(List<TipsBean> tips) {
                this.tips = tips;
            }

            public static class OptionsBean implements Serializable{
                private int id;//"id": "1",
                @SerializedName("option_name")
                private String optionName;//"option_name": "A",
                @SerializedName("option_content")
                private String optionContent;//": "相对于地面来说，受油机是静止的",
                @SerializedName("option_content_html")
                private String optionContentHtml;//"optionContentHtml": "<p>相对于地面来说，受油机是静止的</p>"

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getOptionName() {
                    return optionName;
                }

                public void setOptionName(String optionName) {
                    this.optionName = optionName;
                }

                public String getOptionContent() {
                    return optionContent;
                }

                public void setOptionContent(String optionContent) {
                    this.optionContent = optionContent;
                }

                public String getOptionContentHtml() {
                    return optionContentHtml;
                }

                public void setOptionContentHtml(String optionContentHtml) {
                    this.optionContentHtml = optionContentHtml;
                }
            }

            public static class SubjectTypeBean implements Serializable{
                /**
                 * type_id : 2
                 * type_name : 解答题
                 */

                private String type_id;
                private String type_name;

                public String getType_id() {
                    return type_id;
                }

                public void setType_id(String type_id) {
                    this.type_id = type_id;
                }

                public String getType_name() {
                    return type_name;
                }

                public void setType_name(String type_name) {
                    this.type_name = type_name;
                }
            }

            public static class TipsBean implements Serializable{
                /**
                 * tipName : 正数、负数、有理数、数轴
                 * tipId : 471
                 * version : 1
                 * tipCode : M_C_01_10
                 * parentId : 21
                 * leaf : true
                 * stage : 2
                 * subjectId : 2
                 * levelId : null
                 * qtip : 有理数>>正数、负数、有理数、数轴
                 * tipNamePy : zhengshu、fushu、youlishu、shuzhou
                 * clicks : 0
                 */
                @SerializedName("tip_name")
                private String tipName;
                @SerializedName("tip_id")
                private String tipId;
                private String version;
                @SerializedName("tip_code")
                private String tipCode;
                private String parentId;
                private boolean leaf;
                private int stage;
                @SerializedName("subject_id")
                private String subjectId;
                @SerializedName("level_id")
                private String levelId;
                private String qtip;
                @SerializedName("tip_name_py")
                private String tipNamePy;
                private int clicks;

                public String getTipName() {
                    return tipName;
                }

                public void setTipName(String tipName) {
                    this.tipName = tipName;
                }

                public String getTipId() {
                    return tipId;
                }

                public void setTipId(String tipId) {
                    this.tipId = tipId;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }

                public String getTipCode() {
                    return tipCode;
                }

                public void setTipCode(String tipCode) {
                    this.tipCode = tipCode;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public boolean isLeaf() {
                    return leaf;
                }

                public void setLeaf(boolean leaf) {
                    this.leaf = leaf;
                }

                public int getStage() {
                    return stage;
                }

                public void setStage(int stage) {
                    this.stage = stage;
                }

                public String getSubjectId() {
                    return subjectId;
                }

                public void setSubjectId(String subjectId) {
                    this.subjectId = subjectId;
                }

                public String getLevelId() {
                    return levelId;
                }

                public void setLevelId(String levelId) {
                    this.levelId = levelId;
                }

                public String getQtip() {
                    return qtip;
                }

                public void setQtip(String qtip) {
                    this.qtip = qtip;
                }

                public String getTipNamePy() {
                    return tipNamePy;
                }

                public void setTipNamePy(String tipNamePy) {
                    this.tipNamePy = tipNamePy;
                }

                public int getClicks() {
                    return clicks;
                }

                public void setClicks(int clicks) {
                    this.clicks = clicks;
                }
            }
        }

        public static class TabListBean implements Serializable{
            @SerializedName("tab_id")
            private String tabId;
            @SerializedName("tab_content")
            private String tabContent;
            @SerializedName("tab_content_html")
            private String tabContentHtml;
            @SerializedName("answer_object_id")
            private String answerObjectId;
            @SerializedName("subject_kind_id")
            private String subjectKindId;
            @SerializedName("tab_type")
            private String tabType;
            @SerializedName("tab_type_name")
            private String tabTypeName;
            private String reviewSize;
            @SerializedName("order_id")
            private String orderId;

            public String getTabId() {
                return tabId;
            }

            public void setTabId(String tabId) {
                this.tabId = tabId;
            }

            public String getTabContent() {
                return tabContent;
            }

            public void setTabContent(String tabContent) {
                this.tabContent = tabContent;
            }

            public String getTabContentHtml() {
                return tabContentHtml;
            }

            public void setTabContentHtml(String tabContentHtml) {
                this.tabContentHtml = tabContentHtml;
            }

            public String getAnswerObjectId() {
                return answerObjectId;
            }

            public void setAnswerObjectId(String answerObjectId) {
                this.answerObjectId = answerObjectId;
            }

            public String getSubjectKindId() {
                return subjectKindId;
            }

            public void setSubjectKindId(String subjectKindId) {
                this.subjectKindId = subjectKindId;
            }

            public String getTabType() {
                return tabType;
            }

            public void setTabType(String tabType) {
                this.tabType = tabType;
            }

            public String getTabTypeName() {
                return tabTypeName;
            }

            public void setTabTypeName(String tabTypeName) {
                this.tabTypeName = tabTypeName;
            }

            public String getReviewSize() {
                return reviewSize;
            }

            public void setReviewSize(String reviewSize) {
                this.reviewSize = reviewSize;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }
        }
    }
}

