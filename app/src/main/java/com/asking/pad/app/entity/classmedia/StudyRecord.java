package com.asking.pad.app.entity.classmedia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jswang on 2017/6/9.
 */
@Entity
public class StudyRecord {
    @Id
    private String courseDataId;

    private int playProgress;

    private int playMax;

    private int playPercentage;

    @Generated(hash = 1524881568)
    public StudyRecord(String courseDataId, int playProgress, int playMax,
            int playPercentage) {
        this.courseDataId = courseDataId;
        this.playProgress = playProgress;
        this.playMax = playMax;
        this.playPercentage = playPercentage;
    }

    @Generated(hash = 555891894)
    public StudyRecord() {
    }

    public String getCourseDataId() {
        return this.courseDataId;
    }

    public void setCourseDataId(String courseDataId) {
        this.courseDataId = courseDataId;
    }

    public int getPlayProgress() {
        return this.playProgress;
    }

    public void setPlayProgress(int playProgress) {
        this.playProgress = playProgress;
    }

    public int getPlayMax() {
        return this.playMax;
    }

    public void setPlayMax(int playMax) {
        this.playMax = playMax;
    }

    public int getPlayPercentage() {
        return this.playPercentage;
    }

    public void setPlayPercentage(int playPercentage) {
        this.playPercentage = playPercentage;
    }

}
