package com.asking.pad.app.entity.classmedia;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jswang on 2017/6/1.
 */

public class CourseDataArray implements Parcelable {

    private String courseDataName;

    private String courseDataType;

    private String courseDataUrl;

    public String getCourseDataName() {
        return courseDataName;
    }

    public void setCourseDataName(String courseDataName) {
        this.courseDataName = courseDataName;
    }

    public String getCourseDataType() {
        return courseDataType;
    }

    public void setCourseDataType(String courseDataType) {
        this.courseDataType = courseDataType;
    }

    public String getCourseDataUrl() {
        return courseDataUrl;
    }

    public void setCourseDataUrl(String courseDataUrl) {
        this.courseDataUrl = courseDataUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courseDataName);
        dest.writeString(this.courseDataType);
        dest.writeString(this.courseDataUrl);
    }

    public CourseDataArray() {
    }

    protected CourseDataArray(Parcel in) {
        this.courseDataName = in.readString();
        this.courseDataType = in.readString();
        this.courseDataUrl = in.readString();
    }

    public static final Parcelable.Creator<CourseDataArray> CREATOR = new Parcelable.Creator<CourseDataArray>() {
        @Override
        public CourseDataArray createFromParcel(Parcel source) {
            return new CourseDataArray(source);
        }

        @Override
        public CourseDataArray[] newArray(int size) {
            return new CourseDataArray[size];
        }
    };
}
