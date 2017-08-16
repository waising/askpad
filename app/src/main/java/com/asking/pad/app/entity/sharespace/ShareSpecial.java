package com.asking.pad.app.entity.sharespace;

import android.text.TextUtils;

import com.asking.pad.app.commom.Constants;

import java.util.Date;

/**
 * Created by jswang on 2017/7/17.
 */

public class ShareSpecial {

    public String id;

    public String name;

    public String contentHtml;

    public long endTime;

    public long startTime;

    public int grade;

    public int state;

    public String subject;

    public String seenCount;
    public String interactionCount;
    public String followCount;

    public long deployDate;

    public boolean followed;

    public ShareTeacher teacher;

    public CommunionPlate communionPlate;

    public String getPalteImgUrl() {
        try {
            return communionPlate.palteImgUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public long getCreateDate() {
        try {
            return teacher.createDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getTeaAvatarUrl() {
        try {
            return teacher.askInfo.avatar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getTeaFansNum() {
        try {
            return teacher.getFavorCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean getTeaFavored() {
        try {
            return teacher.isFavored();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getTeaId() {
        try {
            return teacher.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTeaNickName() {
        try {
            return teacher.askInfo.nickName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTeaSubject() {
        try {
            return teacher.askInfo.subject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public String getGradeName() {
        try {
            return Constants.versionTv[grade - 7];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getSubjectName() {
        return TextUtils.equals(subject, "M") ? "数学" : "物理";
    }

    public int getTimeState() {
        long nowTime = new Date().getTime();
        if (nowTime < startTime) {
            return 0;
        } else if (nowTime > endTime) {
            return -1;
        } else {
            return 1;
        }
    }

}
