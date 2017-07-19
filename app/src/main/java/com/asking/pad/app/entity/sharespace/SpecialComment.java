package com.asking.pad.app.entity.sharespace;

/**
 * Created by jswang on 2017/7/19.
 */

public class SpecialComment {

    public String message;

    public int answerCount;

    public long createDate;

    public CommentUser user;

    public class CommentUser {

        public String nickName;

        public String avatar;

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
            return user.avatar;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
