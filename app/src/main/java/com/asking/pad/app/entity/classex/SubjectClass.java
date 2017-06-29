package com.asking.pad.app.entity.classex;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/5/24.
 */

public class SubjectClass {

    @JSONField(name="_id")
    private String subjectid;

    @JSONField(name="difficulty")
    private int difficulty;

    @JSONField(name="subject_description_html")
    private String subjectDescriptionHtml;

    @JSONField(name="right_answer")
    private String rightAnswer;

    @JSONField(name="details_answer_html")
    private String detailsAnswerHtml;

    @JSONField(name="source")
    private String source;

    @JSONField(name="options")
    private List<SubjectOption> options = new ArrayList<>();

    @JSONField(name="subject_type")
    private SubjectType subjectType;

    public String userAnswer;
    public boolean isShowDetailsAnswer;

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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

    public String getDetailsAnswerHtml() {
        return detailsAnswerHtml;
    }

    public void setDetailsAnswerHtml(String detailsAnswerHtml) {
        this.detailsAnswerHtml = detailsAnswerHtml;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<SubjectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SubjectOption> options) {
        this.options = options;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }
}
