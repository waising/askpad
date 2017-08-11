package com.asking.pad.app.entity.superclass;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by jswang on 2017/6/28.
 */

public class  ClassSubject{
    @JSONField(name = "subject_description_html")
    public String subject_description_html;

    @JSONField(name = "subject_description")
    public String subjectDescription;

    @JSONField(name = "details_answer_html")
    public String detailsAnswerHtml;

    public List<ClassOption> options;
}
