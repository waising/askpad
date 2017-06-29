package com.asking.pad.app.entity.superclass;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jswang on 2017/5/24.
 */

public class StudyClassGrade{

    @JSONField(name = "commodityId")
    public String commodityId;

    @JSONField(name = "courseName")
    public String courseName;

    @JSONField(name="purchaseState")
    public String purchaseState;

    public boolean isSelect;

    public boolean getIsBuy(){
        if(TextUtils.equals("0",purchaseState)){
            return  false;
        }
        return  true;
    }
}
