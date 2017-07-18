package com.asking.pad.app.entity.sharespace;

/**
 * Created by jswang on 2017/7/17.
 */

public class ShareSpecial {

    public long endTime;

    public long startTime;

    public ShareTeacher teacher;

    public CommunionPlate communionPlate;

    public String getPalteImgUrl(){
        try{
            return communionPlate.palteImgUrl;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getTeaAvatarUrl(){
        try{
            return teacher.askInfo.avatar;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getTeaNickName(){
        try{
            return teacher.askInfo.nickName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
