package com.asking.pad.app.entity.sharespace;

/**
 * Created by jswang on 2017/7/19.
 */

public class SpecialComment {

    public String message;

    public int answerCount;

    public long createDate;

    public CommentUser user;

    public ShareTeacher teacher;

    public class CommentUser {

        public String id;

        public String nickName;

        public String avatar;

    }

    public String getUserId(){
        try {
            return user.id;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getNickName(){
        try {
            return user.nickName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getAvatarUrl(){
        try {
            if(user !=null){
                return user.avatar;
            }else if(teacher !=null){
                return teacher.askInfo.avatar;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
