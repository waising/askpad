package com.asking.pad.app.entity.classmedia;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by jswang on 2017/6/1.
 */

public class ClassMedia implements Parcelable {

    private String courseTypeName;
    private String byeNumber;
    private String courseDataId;
    private String courseTypeId;
    private String description;
    private String isActive;
    private String sequence;
    private String courseName;
    private String teacher;
    private String teacherImgUrl;
    private String createTime;
    private double coursePrice;
    private String detail;
    private String entryStaff;
    private String targetUser;
    private String courseTypeFullName;

    private String teacherNickName;

    private int playProgress;

    private int playMax;

    private int playPercentage;

    /**
     * 0- 未购买
     * 1-已购买
     */
    private String purchaseState;
    private String purchasedNum;

    private double freeTime;

    /**
     * 0-非赠品
     * 1-赠品
     */
    private int isPresent;

    public String getCourseTypeFullName() {
        return courseTypeFullName;
    }

    public void setCourseTypeFullName(String courseTypeFullName) {
        this.courseTypeFullName = courseTypeFullName;
    }

    public String getTeacherNickName() {
        return teacherNickName;
    }

    public void setTeacherNickName(String teacherNickName) {
        this.teacherNickName = teacherNickName;
    }

    public String getTeacherImgUrl() {
        return teacherImgUrl;
    }

    public void setTeacherImgUrl(String teacherImgUrl) {
        this.teacherImgUrl = teacherImgUrl;
    }

    public int getPlayProgress() {
        return playProgress;
    }

    public void setPlayProgress(int playProgress) {
        this.playProgress = playProgress;
    }

    public int getPlayMax() {
        return playMax;
    }

    public void setPlayMax(int playMax) {
        this.playMax = playMax;
    }

    public int getPlayPercentage() {
        return playPercentage;
    }

    public void setPlayPercentage(int playPercentage) {
        this.playPercentage = playPercentage;
    }

    public String getPrice() {
        return String.valueOf(coursePrice / 100);
    }

    private ArrayList<CourseDataArray> courseDataArray = new ArrayList<>();

    public ArrayList<CourseDataArray> getCourseDataArray() {
        return courseDataArray;
    }

    public void setCourseDataArray(ArrayList<CourseDataArray> courseDataArray) {
        this.courseDataArray = courseDataArray;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public String getByeNumber() {
        return byeNumber;
    }

    public void setByeNumber(String byeNumber) {
        this.byeNumber = byeNumber;
    }

    public String getCourseDataId() {
        return courseDataId;
    }

    public void setCourseDataId(String courseDataId) {
        this.courseDataId = courseDataId;
    }

    public String getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(String courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(int isPresent) {
        this.isPresent = isPresent;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(double coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getEntryStaff() {
        return entryStaff;
    }

    public void setEntryStaff(String entryStaff) {
        this.entryStaff = entryStaff;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(String purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getPurchasedNum() {
        return purchasedNum;
    }

    public void setPurchasedNum(String purchasedNum) {
        this.purchasedNum = purchasedNum;
    }

    public String getPdfUrl() {
        for (CourseDataArray e : getCourseDataArray()) {
            if (TextUtils.equals(e.getCourseDataType(), "ZL02")) {
                return e.getCourseDataUrl();
            }
        }
        return "";
    }

    public String getVideoUrl() {
        for (CourseDataArray e : getCourseDataArray()) {
            if (TextUtils.equals(e.getCourseDataType(), "ZL01")) {
                return e.getCourseDataUrl();
            }
        }
        return "";
    }

    public String getVideoImgUrl() {
        return String.format("%s?vframe/jpg/offset/10/", getVideoUrl());
    }

    public ClassMedia() {
    }

    public double getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(double freeTime) {
        this.freeTime = freeTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courseTypeName);
        dest.writeString(this.byeNumber);
        dest.writeString(this.courseDataId);
        dest.writeString(this.courseTypeId);
        dest.writeString(this.description);
        dest.writeString(this.isActive);
        dest.writeString(this.sequence);
        dest.writeString(this.courseName);
        dest.writeString(this.teacher);
        dest.writeString(this.teacherImgUrl);
        dest.writeString(this.createTime);
        dest.writeDouble(this.coursePrice);
        dest.writeString(this.detail);
        dest.writeString(this.entryStaff);
        dest.writeString(this.targetUser);
        dest.writeString(this.courseTypeFullName);
        dest.writeString(this.teacherNickName);
        dest.writeInt(this.playProgress);
        dest.writeInt(this.playMax);
        dest.writeInt(this.playPercentage);
        dest.writeString(this.purchaseState);
        dest.writeString(this.purchasedNum);
        dest.writeDouble(this.freeTime);
        dest.writeInt(this.isPresent);
        dest.writeTypedList(this.courseDataArray);
    }

    protected ClassMedia(Parcel in) {
        this.courseTypeName = in.readString();
        this.byeNumber = in.readString();
        this.courseDataId = in.readString();
        this.courseTypeId = in.readString();
        this.description = in.readString();
        this.isActive = in.readString();
        this.sequence = in.readString();
        this.courseName = in.readString();
        this.teacher = in.readString();
        this.teacherImgUrl = in.readString();
        this.createTime = in.readString();
        this.coursePrice = in.readDouble();
        this.detail = in.readString();
        this.entryStaff = in.readString();
        this.targetUser = in.readString();
        this.courseTypeFullName = in.readString();
        this.teacherNickName = in.readString();
        this.playProgress = in.readInt();
        this.playMax = in.readInt();
        this.playPercentage = in.readInt();
        this.purchaseState = in.readString();
        this.purchasedNum = in.readString();
        this.freeTime = in.readDouble();
        this.isPresent = in.readInt();
        this.courseDataArray = in.createTypedArrayList(CourseDataArray.CREATOR);
    }

    public static final Creator<ClassMedia> CREATOR = new Creator<ClassMedia>() {
        @Override
        public ClassMedia createFromParcel(Parcel source) {
            return new ClassMedia(source);
        }

        @Override
        public ClassMedia[] newArray(int size) {
            return new ClassMedia[size];
        }
    };
}
