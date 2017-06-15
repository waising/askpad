package com.asking.pad.app.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/4/18.
 */

public class OtoRecord {
    @SerializedName("id")
    public String gid;

    public int state;
    public OtoStudent student;
    public OtoSubject subject;
    public OtoTeacher teacher;
    public OtoTime time;
    public OtoBill bill;

    public class OtoSubject {
        public int grade;
        public String subject;
        public List<SubjectImage> images = new ArrayList<>();
    }

    public class OtoTeacher {
        public String name;
        public String account;
        public String code;
    }

    public class OtoTime {
        public String uploadTime;
        public String holdingSeconds;
        public String connectTime;
        public String endTime;
        public String startTime;
    }
    public class OtoBill {
        public String price;
    }
    public class OtoStudent {
        public Double integral;
    }
}
