package com.asking.pad.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wxwang on 2016/11/29.
 */
public class AskMoneyEntity implements Parcelable {
    private String id;
    private int price;

    public boolean isSelect;

    public String getZengsong() {
        return zengsong;
    }

    public void setZengsong(String zengsong) {
        this.zengsong = zengsong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @SerializedName("value")
    private String money;
    private String zengsong;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.price);
        dest.writeString(this.money);
        dest.writeString(this.zengsong);
    }

    public AskMoneyEntity() {
    }

    protected AskMoneyEntity(Parcel in) {
        this.id = in.readString();
        this.price = in.readInt();
        this.money = in.readString();
        this.zengsong = in.readString();
    }

    public static final Parcelable.Creator<AskMoneyEntity> CREATOR = new Parcelable.Creator<AskMoneyEntity>() {
        @Override
        public AskMoneyEntity createFromParcel(Parcel source) {
            return new AskMoneyEntity(source);
        }

        @Override
        public AskMoneyEntity[] newArray(int size) {
            return new AskMoneyEntity[size];
        }
    };
}

