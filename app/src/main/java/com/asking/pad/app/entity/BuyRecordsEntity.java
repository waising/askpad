package com.asking.pad.app.entity;

import java.util.List;

/**
 * 购买记录
 * Created by zqshen on 2016/12/13.
 */

public class BuyRecordsEntity {


    /**
     * total : 45
     * list : [{"id":"201704261493194064966074","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"0","createDate":1493194064966,"createDate_str":"2017-04-26 16:07:44","products":[{"id":"3834d480-9436-483d-b1d3-ead73c0dbd6b","userId":"567b49556be637e7a5e3cf7e","createDate":1493194064960,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201704251493112803844096","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"50000","order_name":"初中数学-七年级","pay_type":"0","createDate":1493112803844,"createDate_str":"2017-04-25 17:33:23","products":[{"id":"cb4efc44-a631-483a-93a2-7a0afdf0a0ee","userId":"567b49556be637e7a5e3cf7e","stage":"2","versionId":0,"createDate":1493112803837,"commodityId":"f9b69b33-e124-4fbf-a798-a02d1ae46f0d","commodityName":"初中数学-七年级","commodityType":"2","commodityPrice":"50000","subjectCatalogId":"2","subjectCatalogCode":"M","subjectCatalogName":"数学","courseContent":"七年级","months":"12","level_id":"7","num":1,"state":"1"}]},{"id":"201704251493112566009078","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"50000","order_name":"初中数学-七年级","pay_type":"0","createDate":1493112566009,"createDate_str":"2017-04-25 17:29:26","products":[{"id":"1f6d57c8-dce8-4460-8c73-00b03d67a84c","userId":"567b49556be637e7a5e3cf7e","stage":"2","versionId":0,"createDate":1493112566002,"commodityId":"f9b69b33-e124-4fbf-a798-a02d1ae46f0d","commodityName":"初中数学-七年级","commodityType":"2","commodityPrice":"50000","subjectCatalogId":"2","subjectCatalogCode":"M","subjectCatalogName":"数学","courseContent":"七年级","months":"12","level_id":"7","num":1,"state":"1"}]},{"id":"201704251493110423565094","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"50000","order_name":"初中数学-七年级","pay_type":"0","createDate":1493110423565,"createDate_str":"2017-04-25 16:53:43","products":[{"id":"c8a15e50-7460-455c-a18d-0b48ae26f0f1","userId":"567b49556be637e7a5e3cf7e","stage":"2","versionId":0,"createDate":1493110423558,"commodityId":"f9b69b33-e124-4fbf-a798-a02d1ae46f0d","commodityName":"初中数学-七年级","commodityType":"2","commodityPrice":"50000","subjectCatalogId":"2","subjectCatalogCode":"M","subjectCatalogName":"数学","courseContent":"七年级","months":"12","level_id":"7","num":1,"state":"1"}]},{"id":"201704251493109806392065","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"50000","order_name":"初中数学-七年级","pay_type":"0","createDate":1493109806392,"createDate_str":"2017-04-25 16:43:26","products":[{"id":"f07d383a-872f-497e-ad23-fa951423f38a","userId":"567b49556be637e7a5e3cf7e","stage":"2","versionId":0,"createDate":1493109806384,"commodityId":"f9b69b33-e124-4fbf-a798-a02d1ae46f0d","commodityName":"初中数学-七年级","commodityType":"2","commodityPrice":"50000","subjectCatalogId":"2","subjectCatalogCode":"M","subjectCatalogName":"数学","courseContent":"七年级","months":"12","level_id":"7","num":1,"state":"1"}]},{"id":"201704251493109778714079","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"50000","order_name":"初中数学-七年级","pay_type":"0","createDate":1493109778714,"createDate_str":"2017-04-25 16:42:58","products":[{"id":"71a2cbd4-cb91-4276-b92c-37b9d274d312","userId":"567b49556be637e7a5e3cf7e","stage":"2","versionId":0,"createDate":1493109778706,"commodityId":"f9b69b33-e124-4fbf-a798-a02d1ae46f0d","commodityName":"初中数学-七年级","commodityType":"2","commodityPrice":"50000","subjectCatalogId":"2","subjectCatalogCode":"M","subjectCatalogName":"数学","courseContent":"七年级","months":"12","level_id":"7","num":1,"state":"1"}]},{"id":"201704251493106744125033","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"50000","order_name":"初中数学-七年级","pay_type":"0","createDate":1493106744125,"createDate_str":"2017-04-25 15:52:24","products":[{"id":"2913b6c9-6b1e-43f9-868b-af7cec76c4c2","userId":"567b49556be637e7a5e3cf7e","stage":"2","versionId":0,"createDate":1493106744117,"commodityId":"f9b69b33-e124-4fbf-a798-a02d1ae46f0d","commodityName":"初中数学-七年级","commodityType":"2","commodityPrice":"50000","subjectCatalogId":"2","subjectCatalogCode":"M","subjectCatalogName":"数学","courseContent":"七年级","months":"12","level_id":"7","num":1,"state":"1"}]},{"id":"201704251493106685219021","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"50000","order_name":"初中数学-七年级","pay_type":"0","createDate":1493106685219,"createDate_str":"2017-04-25 15:51:25","products":[{"id":"807d87ae-cefd-4d84-97a8-51911f780419","userId":"567b49556be637e7a5e3cf7e","stage":"2","versionId":0,"createDate":1493106685212,"commodityId":"f9b69b33-e124-4fbf-a798-a02d1ae46f0d","commodityName":"初中数学-七年级","commodityType":"2","commodityPrice":"50000","subjectCatalogId":"2","subjectCatalogCode":"M","subjectCatalogName":"数学","courseContent":"七年级","months":"12","level_id":"7","num":1,"state":"1"}]},{"id":"201702151487137843249053","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"10000","order_name":"1000 ","pay_type":"0","createDate":1487137843249,"createDate_str":"2017-02-15 13:50:43","products":[{"id":"41971d7f-1dc8-437c-aa6f-eb6a2b5c3a10","userId":"567b49556be637e7a5e3cf7e","createDate":1487137843242,"recharge":{"id":"56f8eb12e804131665af617e","price":10000,"value":1000},"num":1,"state":"1"}]},{"id":"201701041483519990907015","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"1","createDate":1483519990907,"createDate_str":"2017-01-04 16:53:10","products":[{"id":"5f309492-d806-45d7-9243-c6ced4cd72eb","userId":"567b49556be637e7a5e3cf7e","createDate":1483519990903,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483519509691052","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"1","createDate":1483519509691,"createDate_str":"2017-01-04 16:45:09","products":[{"id":"9ac24852-cd18-44c1-9bd6-ed10b76630fb","userId":"567b49556be637e7a5e3cf7e","createDate":1483519509687,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483519498166076","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"0","createDate":1483519498166,"createDate_str":"2017-01-04 16:44:58","products":[{"id":"91e68933-63d3-4bb7-94c3-612a9e61e103","userId":"567b49556be637e7a5e3cf7e","createDate":1483519498162,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483519104044077","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"1","createDate":1483519104044,"createDate_str":"2017-01-04 16:38:24","products":[{"id":"55153d5e-3901-4d93-aafb-523725a1a7d3","userId":"567b49556be637e7a5e3cf7e","createDate":1483519104040,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483518165533045","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"1","createDate":1483518165533,"createDate_str":"2017-01-04 16:22:45","products":[{"id":"aef07f4f-e709-432a-9e29-58b6cd3bf37b","userId":"567b49556be637e7a5e3cf7e","createDate":1483518165529,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483518053606035","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"1","createDate":1483518053606,"createDate_str":"2017-01-04 16:20:53","products":[{"id":"54af7ea3-01ad-4323-95f9-644230af45cf","userId":"567b49556be637e7a5e3cf7e","createDate":1483518053602,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483518006010096","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"0","createDate":1483518006010,"createDate_str":"2017-01-04 16:20:06","products":[{"id":"d3862702-64f1-4db0-a5ae-05f714a3f89a","userId":"567b49556be637e7a5e3cf7e","createDate":1483518006006,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483517987280086","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"1","createDate":1483517987280,"createDate_str":"2017-01-04 16:19:47","products":[{"id":"c03aaed2-f903-4fd2-897d-4c799de8bbed","userId":"567b49556be637e7a5e3cf7e","createDate":1483517987276,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483517944998071","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"2","createDate":1483517944998,"createDate_str":"2017-01-04 16:19:04","products":[{"id":"34deb3a0-b8ff-46e3-9ef5-7696f0d11166","userId":"567b49556be637e7a5e3cf7e","createDate":1483517944994,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483517940192010","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"1","createDate":1483517940192,"createDate_str":"2017-01-04 16:19:00","products":[{"id":"b070ccaf-0153-45b0-b4ce-9f5b9e479add","userId":"567b49556be637e7a5e3cf7e","createDate":1483517940189,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]},{"id":"201701041483517917355004","userId":"567b49556be637e7a5e3cf7e","state":"3","total_fee":"100","order_name":"10 ","pay_type":"0","createDate":1483517917355,"createDate_str":"2017-01-04 16:18:37","products":[{"id":"34ee75dd-2d96-4efb-8ae5-b744357297d2","userId":"567b49556be637e7a5e3cf7e","createDate":1483517917351,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]}]
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

    public static class ListBean {
        /**
         * id : 201704261493194064966074
         * userId : 567b49556be637e7a5e3cf7e
         * state : 3
         * total_fee : 100
         * order_name : 10
         * pay_type : 0
         * createDate : 1493194064966
         * createDate_str : 2017-04-26 16:07:44
         * products : [{"id":"3834d480-9436-483d-b1d3-ead73c0dbd6b","userId":"567b49556be637e7a5e3cf7e","createDate":1493194064960,"recharge":{"id":"56f8ea15e804131665af617a","price":100,"value":10},"num":1,"state":"1"}]
         */

        private String id;
        private String userId;
        private String state;
        private String total_fee;
        private String order_name;
        private String pay_type;
        private long createDate;
        private String createDate_str;
        private List<ProductsBean> products;

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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public String getOrder_name() {
            return order_name;
        }

        public void setOrder_name(String order_name) {
            this.order_name = order_name;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getCreateDate_str() {
            return createDate_str;
        }

        public void setCreateDate_str(String createDate_str) {
            this.createDate_str = createDate_str;
        }

        public List<ProductsBean> getProducts() {
            return products;
        }

        public void setProducts(List<ProductsBean> products) {
            this.products = products;
        }

        public static class ProductsBean {
            /**
             * id : 3834d480-9436-483d-b1d3-ead73c0dbd6b
             * userId : 567b49556be637e7a5e3cf7e
             * createDate : 1493194064960
             * recharge : {"id":"56f8ea15e804131665af617a","price":100,"value":10}
             * num : 1
             * state : 1
             */

            private String id;
            private String userId;
            private long createDate;
            private RechargeBean recharge;
            private int num;
            private String state;
            private String commodityType;

            private String versionName;

            private String months;
            private String courseContent;

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

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public String getCommodityType() {
                return commodityType;
            }

            public void setCommodityType(String commodityType) {
                this.commodityType = commodityType;
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

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public RechargeBean getRecharge() {
                return recharge;
            }

            public void setRecharge(RechargeBean recharge) {
                this.recharge = recharge;
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

            public static class RechargeBean {
                /**
                 * id : 56f8ea15e804131665af617a
                 * price : 100
                 * value : 10
                 */

                private String id;
                private int price;
                private int value;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
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
}
