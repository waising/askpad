package com.asking.pad.app.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.commom.CommonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/3/2.
 */

public class ExerAskEntity implements Serializable {

    public String _id;
    public String name;
    public String subject_description_html;
    public String right_answer;
    public String details_answer_html;
    public int difficulty;

    public String xiangxjt;
    public String xiangjbj;
    public String qiaoxqj;

    public SubjectType subject_type;

    public boolean showReult = false;
    public boolean showReultBtn = false;

    public boolean  isExpand = false;

    public List<OptionsBean> options = new ArrayList<>();

    public List<OptionsBean> biansts = new ArrayList<>();

    public List<OptionsBean> bianst = new ArrayList<>();

    public String silyx;

    public static class SubjectType implements Serializable{
        /**
         * 1-选择题  2-解答题
         */
        public String type_id;
        public String type_name;
    }

    public static class OptionsBean implements Serializable{
        public String _id;
        public String option_name;
        public String option_content_html;

        public String subject_description_html;
        public String details_answer_html;

        public List<OptionsBean> options = new ArrayList<>();

        public String getDetailsOptions(){
            StringBuffer s = new StringBuffer();
            s.append(subject_description_html).append("\n");
            for(OptionsBean e:options){
                s.append("<p>")
                .append(e.option_name).append(":  ")
                .append(CommonUtil.getReplaceStr(e.option_content_html,"<p>","</p>"))
                .append("</p>");
            }
            return s.toString();
        }

        private ResultEntity resultEntity;
        public boolean isSelect = false;

        public String getOptionName() {
            return option_name;
        }


        public String getOptionContentHtml() {
            return option_content_html;
        }


        public void setResultEntity(ResultEntity resultEntity){
            this.resultEntity = resultEntity;
        }

        public ResultEntity getResultEntity(){
            return resultEntity;
        }

    }

    public String getSubjectTypeId(){
        if(subject_type != null ){
            return subject_type.type_id;
        }
        return  "";
    }

    public String getSilyx(){
        try {
            JSONObject jsonObj = JSON.parseObject(silyx);
            return  jsonObj.getString("content");
        }catch(Exception e){}
        return  "";
    }

    public String getXiangjbj(){
        try {
            JSONObject jsonObj = JSON.parseObject(xiangjbj);
            return  jsonObj.getString("content");
        }catch(Exception e){}
        return  "";
    }

    public String getQiaoxqj(){
        try {
            JSONObject jsonObj = JSON.parseObject(qiaoxqj);
            return  jsonObj.getString("content");
        }catch(Exception e){}
        return  "";
    }

    public List<OptionsBean>  getOptions(){
        return  options;
    }

}
