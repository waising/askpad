package com.asking.pad.app.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 答疑记录
 * Created by jswang on 2017/4/18.
 */

public class RecordAnswer implements Serializable{
    @JSONField(name="id")
    public String orderId;

    public int state;
    public OtoStudent student;
    public OtoSubject subject;
    public OtoTeacher teacher;
    public OtoTime time;
    public OtoBill bill;
    public StuEvaluation evaluation;

    public List<Object> videos = new ArrayList<>();


    public class OtoSubject implements Serializable{
        public int grade;
        public String subject;
        public List<SubjectImage> images = new ArrayList<>();
    }

    public class OtoTeacher implements Serializable{
        public String avatar;
        public String name;
        public String account;
        public String code;
    }

    public class OtoTime implements Serializable{
        public String uploadTime;
        public String holdingSeconds;
        public String connectTime;
        public String endTime;
        public String startTime;
    }

    public class OtoBill implements Serializable{
        public String price;
    }

    public class OtoStudent implements Serializable{
        public Double integral;
    }

    public class StuEvaluation implements Serializable{
        public int star;
        public String suggest;
        public String reward;
    }

    public String getVideoUrl(){
        try {
            String videoUrl = videos.get(0).toString();
            return videoUrl;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getImgUrl(){
        try {
            String imgUrl = subject.images.get(0).url;
            return imgUrl;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
