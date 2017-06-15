package com.asking.pad.app.entity;

import android.os.Environment;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.ui.downbook.download.DownState;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.File;

/**
 * Created by jswang on 2017/5/2.
 */

@Entity
public class BookInfo {
    @Id
    @JSONField(name="id")
    private String bookId;

    @JSONField(name="url")
    private String url;

    @JSONField(name="version_name")
    private String versionName;

    @JSONField(name="version_id")
    private String versionId;

    @JSONField(name="subject_catalog_name")
    private String subjectCatalogName;

    @JSONField(name="subject_catalog_code")
    private String subjectCatalogCode;

    @JSONField(name="version")
    private String version;

    @JSONField(name="stage")
    private String stage;

    @JSONField(name="level_id")
    private String levelId;

    @JSONField(name="level_name")
    private String levelName;

    private String userId;

    private byte downState = DownState.START;

    /**文件总长度*/
    private long countLength;
    /**下载长度*/
    private long readLength;

    @Generated(hash = 1739903855)
    public BookInfo(String bookId, String url, String versionName, String versionId, String subjectCatalogName,
            String subjectCatalogCode, String version, String stage, String levelId, String levelName,
            String userId, byte downState, long countLength, long readLength) {
        this.bookId = bookId;
        this.url = url;
        this.versionName = versionName;
        this.versionId = versionId;
        this.subjectCatalogName = subjectCatalogName;
        this.subjectCatalogCode = subjectCatalogCode;
        this.version = version;
        this.stage = stage;
        this.levelId = levelId;
        this.levelName = levelName;
        this.userId = userId;
        this.downState = downState;
        this.countLength = countLength;
        this.readLength = readLength;
    }

    @Generated(hash = 1952025412)
    public BookInfo() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getSubjectCatalogName() {
        return subjectCatalogName;
    }

    public void setSubjectCatalogName(String subjectCatalogName) {
        this.subjectCatalogName = subjectCatalogName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getSubjectCatalogCode() {
        return subjectCatalogCode;
    }

    public void setSubjectCatalogCode(String subjectCatalogCode) {
        this.subjectCatalogCode = subjectCatalogCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSavePath() {
        return getBookDirPath(String.format("%s%s_%s_%s", getSubjectCatalogCode(),getStage(),getVersionId()
                ,TextUtils.isEmpty(getLevelId())?"":getLevelId()));
    }

    public String getBookUnZipPath() {
        return getSavePath()+"_dir";
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public static String getBookDirPath(String image) {
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
        return file.getAbsolutePath() + "/" + image;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BookInfo))
            return false;
        if (obj == this)
            return true;
        return TextUtils.equals(this.bookId,((BookInfo) obj).bookId);
    }

    public String getLevelId() {
        return this.levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public void deleteFile(){
        new Thread(){
            @Override
            public void run() {
                FileUtils.deleteFile(new File(getSavePath()));
                FileUtils.deleteDir(new File(getBookUnZipPath()));
            }
        }.start();
    }
}
