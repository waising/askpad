package com.asking.pad.app.entity.sharespace;

import java.util.List;

/**
 * Created by linbin on 2017/7/19.
 */

public class MyAttention {


    private String id;
    private String password;
    private int olState;
    private int acState;
    private String org;
    private InfoBean info;
    private PayInfoBean payInfo;
    private AskInfoBean askInfo;
    private long createDate;
    private int topicCount;

    public int getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getOlState() {
        return olState;
    }

    public void setOlState(int olState) {
        this.olState = olState;
    }

    public int getAcState() {
        return acState;
    }

    public void setAcState(int acState) {
        this.acState = acState;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public PayInfoBean getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfoBean payInfo) {
        this.payInfo = payInfo;
    }

    public AskInfoBean getAskInfo() {
        return askInfo;
    }

    public void setAskInfo(AskInfoBean askInfo) {
        this.askInfo = askInfo;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public static class InfoBean {
        /**
         * name : 测试
         * familyName : 测
         * school : 测试
         * email : null
         * remark : 测试
         */

        private String name;
        private String familyName;
        private String school;
        private Object email;
        private String remark;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public static class PayInfoBean {
        private List<?> bandCards;

        public List<?> getBandCards() {
            return bandCards;
        }

        public void setBandCards(List<?> bandCards) {
            this.bandCards = bandCards;
        }
    }

    public static class AskInfoBean {
        /**
         * nickName : 测试
         * avatar : http://stcdn.91asking.com/83de3da4-29dd-4ae6-894a-dc44a5f3e48f1499754784531
         * exprience : 5
         * level : 助教
         * star : 0.25
         * answerTimes : 4
         * code : CM_07001
         * subject : 初三数学
         * askcoin : 53
         */

        private String nickName;
        private String avatar;
        private int exprience;
        private String level;
        private double star;
        private int answerTimes;
        private String code;
        private String subject;
        private int askcoin;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getExprience() {
            return exprience;
        }

        public void setExprience(int exprience) {
            this.exprience = exprience;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public double getStar() {
            return star;
        }

        public void setStar(double star) {
            this.star = star;
        }

        public int getAnswerTimes() {
            return answerTimes;
        }

        public void setAnswerTimes(int answerTimes) {
            this.answerTimes = answerTimes;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public int getAskcoin() {
            return askcoin;
        }

        public void setAskcoin(int askcoin) {
            this.askcoin = askcoin;
        }
    }
}
