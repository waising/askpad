package com.asking.pad.app.entity;

import java.util.List;

/**
 * 收藏夹entitiy
 */

public class CollectionEntity {


    /**
     * total : 13
     * list : [{"id":"56d907200cf244dd39a80d89","type":"1","tille":"有理数的加法","objId":"2273","createTime":1487141610282,"createTime_fmt":"2017-02-15 14:53:30"},{"id":"589c17100cf2f45ca7f3b133","type":"1","tille":"数轴","objId":"3033","km":"M2","createTime":1486624528991,"createTime_fmt":"2017-02-09 15:15:28"},{"id":"589ad4bd0cf2ec7771ed4f97","type":"1","tille":"有理数的定义","objId":"3030","km":"M2","createTime":1486542013416,"createTime_fmt":"2017-02-08 16:20:13"},{"id":"587d7f690cf26e823545c2e6","type":"1","tille":"对顶角","objId":"3117","km":"M2","createTime":1484619625878,"createTime_fmt":"2017-01-17 10:20:25"},{"id":"58796e900cf23d1c1062f66a","type":"1","tille":"几何图形","objId":"2352","km":"M2","createTime":1484353168891,"createTime_fmt":"2017-01-14 08:19:28"},{"id":"585cd1ff0cf278176ab9216b","type":"1","tille":"不可能事件","objId":"1285","km":"M2","createTime":1482478093136,"createTime_fmt":"2016-12-23 15:28:13"}]
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
         * id : 56d907200cf244dd39a80d89
         * type : 1
         * tille : 有理数的加法
         * objId : 2273
         * createTime : 1487141610282
         * createTime_fmt : 2017-02-15 14:53:30
         * km : M2
         */

        private String id;
        private String type;
        private String tille;
        private String objId;
        private long createTime;
        private String createTime_fmt;
        private String km;




        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTille() {
            return tille;
        }

        public void setTille(String tille) {
            this.tille = tille;
        }

        public String getObjId() {
            return objId;
        }

        public void setObjId(String objId) {
            this.objId = objId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateTime_fmt() {
            return createTime_fmt;
        }

        public void setCreateTime_fmt(String createTime_fmt) {
            this.createTime_fmt = createTime_fmt;
        }

        public String getKm() {
            return km;
        }

        public void setKm(String km) {
            this.km = km;
        }
    }
}
