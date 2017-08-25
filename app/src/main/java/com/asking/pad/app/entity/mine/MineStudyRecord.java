package com.asking.pad.app.entity.mine;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/6/29.
 */

public class MineStudyRecord implements Serializable{

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

    public String scheduleTitle;

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


    /**
     * 0-同步课堂  1-初升高衔接课
     * @return
     */
    public int getDataType(){
        int type = -1;
        try{
            String[] str = commodityId.split("-");
            if(TextUtils.equals(str[1],"KC03")){
                type = 0;
            }else if(TextUtils.equals(str[1],"KC01")){
                type = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return type;
    }

    /**
     * 如：初中数学-XK01
     * @return
     */
    public String getClassId(){
        try{
            String[] str = commodityId.split("-");
            return str[2];
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 如：人教版-BB01
     * @return
     */
    public String getVersionId(){
        try{
            String[] str = commodityId.split("-");
            return str[3];
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 如：七年级下-CS0701
     * @return
     */
    public String geGradeId(){
        try{
            String[] str = commodityId.split("-");
            return str[4];
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
