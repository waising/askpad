package com.asking.pad.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wxwang on 2016/11/1.
 */
public class ShopCartEntity implements Serializable{


    /**
     * total : 2
     * list : [{"id":"59083a618552ee00012c7b0c","userId":"5681dd7d1825d6115cae0932","stage":"2","versionId":16,"versionName":"人教版","createDate":1493711457495,"commodityId":"f9b69b33-e124-4fbf-a798-a02d1ae46f0d","commodityName":"初中数学-七年级","commodityType":"2","commodityPrice":"50000","subjectCatalogId":"2","subjectCatalogCode":"M","subjectCatalogName":"数学","courseContent":"七年级","months":"12","level_id":"7","num":1,"state":"0"},{"id":"59083a6a8552ee00012c7b0d","userId":"5681dd7d1825d6115cae0932","stage":"2","versionId":16,"versionName":"人教版","createDate":1493711466446,"commodityId":"f9b69b33-e124-4fbf-a798-a02d1ae46f0d","commodityName":"初中数学-七年级","commodityType":"2","commodityPrice":"50000","subjectCatalogId":"2","subjectCatalogCode":"M","subjectCatalogName":"数学","courseContent":"七年级","months":"12","level_id":"7","num":1,"state":"0"}]
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * id : 59083a618552ee00012c7b0c
         * userId : 5681dd7d1825d6115cae0932
         * stage : 2
         * versionId : 16
         * versionName : 人教版
         * createDate : 1493711457495
         * commodityId : f9b69b33-e124-4fbf-a798-a02d1ae46f0d
         * commodityName : 初中数学-七年级
         * commodityType : 2
         * commodityPrice : 50000
         * subjectCatalogId : 2
         * subjectCatalogCode : M
         * subjectCatalogName : 数学
         * courseContent : 七年级
         * months : 12
         * level_id : 7
         * num : 1
         * state : 0
         */

        private String id;
        private String userId;
        private String stage;
        private int versionId;
        private String versionName;
        private long createDate;
        private String commodityId;
        private String commodityName;
        private String commodityType;
        private String commodityPrice;
        private String subjectCatalogId;
        private String subjectCatalogCode;
        private String subjectCatalogName;
        private String courseContent;
        private String months;
        private String level_id;
        private int num;
        private String state;

        private RechargeBean recharge;


        private boolean isChoose;//是否选中标记

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public RechargeBean getRecharge() {
            return recharge;
        }

        public void setRecharge(RechargeBean recharge) {
            this.recharge = recharge;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(String commodityId) {
            this.commodityId = commodityId;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public String getCommodityType() {
            return commodityType;
        }

        public void setCommodityType(String commodityType) {
            this.commodityType = commodityType;
        }

        public String getCommodityPrice() {
            return commodityPrice;
        }

        public void setCommodityPrice(String commodityPrice) {
            this.commodityPrice = commodityPrice;
        }

        public String getSubjectCatalogId() {
            return subjectCatalogId;
        }

        public void setSubjectCatalogId(String subjectCatalogId) {
            this.subjectCatalogId = subjectCatalogId;
        }

        public String getSubjectCatalogCode() {
            return subjectCatalogCode;
        }

        public void setSubjectCatalogCode(String subjectCatalogCode) {
            this.subjectCatalogCode = subjectCatalogCode;
        }

        public String getSubjectCatalogName() {
            return subjectCatalogName;
        }

        public void setSubjectCatalogName(String subjectCatalogName) {
            this.subjectCatalogName = subjectCatalogName;
        }

        public String getCourseContent() {
            return courseContent;
        }

        public void setCourseContent(String courseContent) {
            this.courseContent = courseContent;
        }

        public String getMonths() {
            return months;
        }

        public void setMonths(String months) {
            this.months = months;
        }

        public String getLevel_id() {
            return level_id;
        }

        public void setLevel_id(String level_id) {
            this.level_id = level_id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }


        public static class RechargeBean implements Serializable{
            /**
             * id : 56f8ea15e804131665af617a
             * price : 100
             * value : 10
             */

            private String id;
            private String price;
            private int value;//阿思币数量

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }
}
