package com.asking.pad.app.entity.superclass.exer;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/6/29.
 */

public class SubjectExerEntity {
    @JSONField(name = "_id")
    private String subjectId;

    @JSONField(name = "subject_description")
    private String subjectDescription;

    @JSONField(name = "subject_description_html")
    private String subjectDescriptionHtml;

    @JSONField(name = "details_answer")
    private String detailsAnswer;

    @JSONField(name = "details_answer_html")
    private String detailsAnswerHtml;

    @JSONField(name = "right_answer")
    private String rightAnswer;

    @JSONField(name = "difficulty")
    private int difficulty;

    @JSONField(name = "subject_catalog")
    private SubjectCatalog subjectCatalog;

    @JSONField(name = "subject_type")
    private SubjectType subjectType;

    @JSONField(name = "options")
    private List<SubjectOption> options = new ArrayList<>();

    public String userAnswer;
    public boolean isShowDetailsAnswer;


    public String getSubjectCatalogCode() {
        String ss = "";
        if(getSubjectCatalog()!=null){
            ss = subjectCatalog.getSubjectCatalogCode()+subjectCatalog.getSubjectCatalogId();
        }
        return ss;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public SubjectCatalog getSubjectCatalog() {
        return subjectCatalog;
    }

    public void setSubjectCatalog(SubjectCatalog subjectCatalog) {
        this.subjectCatalog = subjectCatalog;
    }

    public class SubjectCatalog {
        @JSONField(name = "subject_catalog_id")
        private String subjectCatalogId;

        @JSONField(name = "subject_catalog_code")
        private String subjectCatalogCode;

        public String getSubjectCatalogCode() {
            return subjectCatalogCode;
        }

        public void setSubjectCatalogCode(String subjectCatalogCode) {
            this.subjectCatalogCode = subjectCatalogCode;
        }

        public String getSubjectCatalogId() {
            return subjectCatalogId;
        }

        public void setSubjectCatalogId(String subjectCatalogId) {
            this.subjectCatalogId = subjectCatalogId;
        }
    }

    public class SubjectType {
        @JSONField(name = "type_id")
        private String typeId;

        @JSONField(name = "type_name")
        private String typeName;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    public class SubjectOption {
        @JSONField(name = "option_name")
        private String optionName;

        @JSONField(name = "option_content")
        private String optionContent;

        @JSONField(name = "option_content_html")
        private String optionContentHtml;

        public boolean isSelect;

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


    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public List<SubjectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SubjectOption> options) {
        this.options = options;
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
