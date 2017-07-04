package com.asking.pad.app.entity.book;

import android.os.Environment;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.ui.downbook.download.DownState;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.File;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jswang on 2017/5/2.
 */

@Entity
public class BookDownInfo {
    @Id
    @JSONField(name="commodityId")
    private String commodityId;

    @JSONField(name="DownloadUrl")
    private String downloadUrl;

    @JSONField(name="courseName")
    private String courseName;

    @JSONField(name="commodityName")
    private String commodityName;

    @JSONField(name="courseTypeId")
    private String courseTypeId;

    @JSONField(name="version")
    private String version;

    /**
     * 0-不需要更新  1-需要更新
     */
    @JSONField(name="update")
    private int update = 0;

    private String userId;

    private byte downState = DownState.START;

    /**文件总长度*/
    private long countLength;
    /**下载长度*/
    private long readLength;

    @Generated(hash = 848508106)
    public BookDownInfo(String commodityId, String downloadUrl, String courseName, String commodityName,
            String courseTypeId, String version, int update, String userId, byte downState, long countLength,
            long readLength) {
        this.commodityId = commodityId;
        this.downloadUrl = downloadUrl;
        this.courseName = courseName;
        this.commodityName = commodityName;
        this.courseTypeId = courseTypeId;
        this.version = version;
        this.update = update;
        this.userId = userId;
        this.downState = downState;
        this.countLength = countLength;
        this.readLength = readLength;
    }

    @Generated(hash = 2101010063)
    public BookDownInfo() {
    }

    public int getUpdate() {
        return update;
    }

    public void setUpdate(int update) {
        this.update = update;
    }

    public String getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(String courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte getDownState() {
        return downState;
    }

    public void setDownState(byte downState) {
        this.downState = downState;
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

    public String getSavePath() {
        return getBookDirPath(getCommodityId());
    }

    public static String getBookDirPath(String commodityId) {
        File file1 = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_BOOK_PATH);
        if (file1.exists() && !file1.isDirectory()) {
            file1.delete();
            file1.mkdirs();
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_BOOK_PATH);
        if (!file.exists()) {
            file.mkdirs();
        } else if (!file.isDirectory()) {
            FileUtils.deleteDir(file);
            file.mkdirs();
        }
        return file.getAbsolutePath() + "/" + commodityId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BookDownInfo))
            return false;
        if (obj == this)
            return true;
        return TextUtils.equals(this.getCommodityId(),((BookDownInfo) obj).getCommodityId());
    }
}
