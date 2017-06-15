package com.asking.pad.app.entity;

import java.util.List;

/**
 * 笔记model
 */

public class NoteEntity {

    /**
     * total : 2
     * list : [{"id":"58e6f805b3f3b5000155f3b3","userId":"5681dd7d1825d6115cae0932","content":"1、老师上课时使用\u201c小蜜蜂\u201d扩音器是为了增大声音的（　\t\n响度　）","createTime":1491531781091,"createTime_fmt":"2017-04-07 10:23:01","state":1},{"id":"56a38e480cf287b0daa3f48e","userId":"5681dd7d1825d6115cae0932","content":"测试hhhhh一次函数的图象是一条直线","createTime":1482313138794,"createTime_fmt":"2016-12-21 17:38:58","state":1}]
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
         * id : 58e6f805b3f3b5000155f3b3
         * userId : 5681dd7d1825d6115cae0932
         * content : 1、老师上课时使用“小蜜蜂”扩音器是为了增大声音的（
         * 响度　）
         * createTime : 1491531781091
         * createTime_fmt : 2017-04-07 10:23:01
         * state : 1
         */

        private String id;
        private String userId;
        private String content;
        private long createTime;
        private String createTime_fmt;
        private int state;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
