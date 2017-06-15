package com.asking.pad.app.entity;

/**
 * Created by wxwang on 2016/11/30.
 */
public class ShopCartPayEntity {
    private String orderId;
    private String type;
    private String payType;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}

