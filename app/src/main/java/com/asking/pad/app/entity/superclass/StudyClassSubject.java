package com.asking.pad.app.entity.superclass;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/5/24.
 */

public class StudyClassSubject {

    @JSONField(name="value")
    public StudyClassVersion value;

    @JSONField(name="nodelist")
    public List<StudyClassSubject> nodelist = new ArrayList<>();

    public boolean isSelect;

    public StudyClassSubject(){

    }

    public StudyClassSubject(String versionName){
        value = new StudyClassVersion();
        value.productName = versionName;
        this.dataType = 1;
    }

    /**
     * 0-版本  1-精学
     */
    public int dataType = 0;

    public String getProductId(){
        if(value !=null){
            return value.productId;
        }
        return "";
    }

    public String getProductName(){
        if(value !=null){
            return value.productName;
        }
        return "";
    }

    public List<StudyClassGrade> getGradeList(){
        List<StudyClassGrade> courseList = new ArrayList<>();
        if(value !=null){
            courseList.addAll(value.courseList);
        }
        return courseList;
    }

}
