package com.asking.pad.app.entity.mine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/6/29.
 */

public class MineStudyRecord {

    public String commodityId;

    /**
     *  TC - 全套
     * KC - 课程
     */
    public String commodityType;

    /**
     * 2017-06-29 16:42:56
     */
    public String createTime;

    public double schedulePercent;

    /**
     * 初升高数学测试1
     */
    public String commodityName;

    /**
     * 同步课堂-初中数学
     */
    public String commodityTypeName;

    /**
     * 全套子课程列表（当类型为TC时有值）
     */
    public List<MineStudyRecord> childCommodityList = new ArrayList<>();
}
