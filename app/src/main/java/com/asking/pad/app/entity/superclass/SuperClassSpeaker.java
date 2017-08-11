package com.asking.pad.app.entity.superclass;

import android.text.TextUtils;

import com.asking.pad.app.entity.classex.SubjectClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/6/28.
 */

public class SuperClassSpeaker {
    public String subjectKindName;

    public String subjectKindType;

    public ClassSubject subject;

    public List<ClassTab> tabList;

    public List<SubjectClass> subjectmuls = new ArrayList<>();

    public String getSubjectDescriptionHtml() {
        StringBuffer str = new StringBuffer("");
        try {
            str = new StringBuffer(subject.subject_description_html);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (TextUtils.equals(subjectKindType, "1")) {
                for (ClassOption e : subject.options) {
                    str.append("<p>").append(e.optionName)
                            .append(e.optionContentHtml.replace("<p>", "").replace("</p>", "")).append("</p>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public List<ClassTab> getClassTabList() {
        if (tabList == null) {
            tabList = new ArrayList<>();
        }
        return tabList;
    }
}
