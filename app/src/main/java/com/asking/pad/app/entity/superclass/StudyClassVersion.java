package com.asking.pad.app.entity.superclass;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/5/24.
 */

public class StudyClassVersion{
    @JSONField(name="productName")
    public String productName;

    @JSONField(name="productId")
    public String productId;

    @JSONField(name="courseList")
    public List<StudyClassGrade> courseList = new ArrayList<>();
}
