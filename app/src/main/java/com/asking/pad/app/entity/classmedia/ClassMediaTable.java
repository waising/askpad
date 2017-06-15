package com.asking.pad.app.entity.classmedia;

import android.os.Parcel;
import android.os.Parcelable;

import com.asking.pad.app.ui.downbook.download.DownState;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jswang on 2017/6/12.
 */

@Entity
public class ClassMediaTable implements Parcelable {
    @Id
    private String courseDataId;
    private String courseName;
    private String videoUrl;
    private String pdfUrl;

    private String userId;

    private byte downState = DownState.START;

    /**文件总长度*/
    private long countLength;
    /**下载长度*/
    private long readLength;

    @Generated(hash = 580625126)
    public ClassMediaTable(String courseDataId, String courseName, String videoUrl,
            String pdfUrl, String userId, byte downState, long countLength,
            long readLength) {
        this.courseDataId = courseDataId;
        this.courseName = courseName;
        this.videoUrl = videoUrl;
        this.pdfUrl = pdfUrl;
        this.userId = userId;
        this.downState = downState;
        this.countLength = countLength;
        this.readLength = readLength;
    }

    @Generated(hash = 250948586)
    public ClassMediaTable() {
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public  String getVideoImgUrl(){
        return String.format("%s?vframe/jpg/offset/10/",getVideoUrl());
    }

    public String getCourseDataId() {
        return courseDataId;
    }

    public void setCourseDataId(String courseDataId) {
        this.courseDataId = courseDataId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public byte getDownState() {
        return downState;
    }

    public void setDownState(byte downState) {
        this.downState = downState;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courseDataId);
        dest.writeString(this.courseName);
        dest.writeString(this.videoUrl);
        dest.writeString(this.pdfUrl);
        dest.writeString(this.userId);
        dest.writeByte(this.downState);
        dest.writeLong(this.countLength);
        dest.writeLong(this.readLength);
    }

    protected ClassMediaTable(Parcel in) {
        this.courseDataId = in.readString();
        this.courseName = in.readString();
        this.videoUrl = in.readString();
        this.pdfUrl = in.readString();
        this.userId = in.readString();
        this.downState = in.readByte();
        this.countLength = in.readLong();
        this.readLength = in.readLong();
    }

    public static final Parcelable.Creator<ClassMediaTable> CREATOR = new Parcelable.Creator<ClassMediaTable>() {
        @Override
        public ClassMediaTable createFromParcel(Parcel source) {
            return new ClassMediaTable(source);
        }

        @Override
        public ClassMediaTable[] newArray(int size) {
            return new ClassMediaTable[size];
        }
    };
}
