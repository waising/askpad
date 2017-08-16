package com.asking.pad.app.entity.sharespace;

/**
 * Created by jswang on 2017/7/18.
 */

public class ShareTeacher {

    public TeaAskInfo askInfo;
    /**
     * 粉丝数
     */
    private int favorCount;
    /**
     * 是否被关注
     */
    private boolean favored;

    public long createDate;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFavorCount() {
        return favorCount;
    }

    public void setFavorCount(int favorCount) {
        this.favorCount = favorCount;
    }

    public boolean isFavored() {
        return favored;
    }

    public void setFavored(boolean favored) {
        this.favored = favored;
    }


}
