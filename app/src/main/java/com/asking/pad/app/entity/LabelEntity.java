package com.asking.pad.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wxwang on 2016/11/14.
 */
public class LabelEntity implements Parcelable {

    public LabelEntity(int icon,String name){
        this.icon = icon;
        this.name = name;
    }

    public LabelEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public LabelEntity(String id, String name,Boolean isSelect) {
        this.id = id;
        this.name = name;
        this.isSelect = isSelect;
    }

    public String[] values;
    public LabelEntity(String id, String name,Boolean isSelect,String... values) {
        this.id = id;
        this.name = name;
        this.isSelect = isSelect;
        this.values = values;
    }

    public LabelEntity(int type,String id, String name,Boolean isSelect) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.isSelect = isSelect;
    }

    public LabelEntity(int type,int levelId,String id, String name,Boolean isSelect) {
        this.levelId = levelId;
        this.type = type;
        this.id = id;
        this.name = name;
        this.isSelect = isSelect;
    }

    public LabelEntity(int icon,String name,Boolean isSelect) {
        this.icon = icon;
        this.name = name;
        this.isSelect = isSelect;
    }

    public LabelEntity(String id,String tmpId, String name) {
        this.id = id;
        this.tmpId = tmpId;
        this.name = name;
    }

    public LabelEntity(int icon,String id,String name){
        this.icon = icon;
        this.id = id;
        this.name = name;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    private int levelId;

    private int type;
    private String name;
    private String id;
    private Boolean isSelect = false;

    public String getTmpId() {
        return tmpId;
    }

    public void setTmpId(String tmpId) {
        this.tmpId = tmpId;
    }

    private String tmpId;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private int icon;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.tmpId);
        dest.writeInt(this.icon);
    }

    protected LabelEntity(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.tmpId = in.readString();
        this.icon = in.readInt();
    }

    public static final Creator<LabelEntity> CREATOR = new Creator<LabelEntity>() {
        @Override
        public LabelEntity createFromParcel(Parcel source) {
            return new LabelEntity(source);
        }

        @Override
        public LabelEntity[] newArray(int size) {
            return new LabelEntity[size];
        }
    };
}

