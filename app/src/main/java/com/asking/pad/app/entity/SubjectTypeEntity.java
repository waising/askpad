package com.asking.pad.app.entity;

import java.util.List;

/**
 * 服务器只返回给我们题目的id和类型，题目标题和解析得再次请求
 */

public class SubjectTypeEntity {

    /**
     * total : 5
     * list : [{"id":"581176b30cf22062157791d0","type":"2","objId":"1ADE62E8-E7E2-47E1-B6F4-FAD16238606E","km":"M2","createTime":1477539507969,"createTime_fmt":"2016-10-27 11:38:27"},{"id":"57cccb3e0cf2ff8e35d2e981","type":"2","objId":"8C369C15-AF7E-4824-B539-5191B746A05E","km":"M2","createTime":1473039166630,"createTime_fmt":"2016-09-05 09:32:46"},{"id":"57a156a50cf2cf6b5ff04f98","type":"2","objId":"F775585A-E274-4C34-A99B-7A5BF6472E2E","km":"M2","createTime":1470191269262,"createTime_fmt":"2016-08-03 10:27:49"},{"id":"56f92dc20cf2237123f3108e","type":"2","objId":"1FFA8817-DF7B-4033-9102-49D3D10A7C8E","km":"M2","createTime":1459170754470,"createTime_fmt":"2016-03-28 21:12:34"},{"id":"56f35b410cf2453428b54a18","type":"2","objId":"9E777FCF-FD27-445D-B99D-92AA5E387700","km":"M2","createTime":1458789185263,"createTime_fmt":"2016-03-24 11:13:05"}]
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
         * id : 581176b30cf22062157791d0
         * type : 2
         * objId : 1ADE62E8-E7E2-47E1-B6F4-FAD16238606E
         * km : M2
         * createTime : 1477539507969
         * createTime_fmt : 2016-10-27 11:38:27
         */

        private String id;
        private String type;
        private String objId;
        private String km;
        private long createTime;
        private String createTime_fmt;

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

        public String getObjId() {
            return objId;
        }

        public void setObjId(String objId) {
            this.objId = objId;
        }

        public String getKm() {
            return km;
        }

        public void setKm(String km) {
            this.km = km;
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
    }
}
