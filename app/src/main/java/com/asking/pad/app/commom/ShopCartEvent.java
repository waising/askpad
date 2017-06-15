package com.asking.pad.app.commom;

/**
 *购物车流程的Event事件
 */

public class ShopCartEvent {

    /**
     * 跳转到下一页
     */
    public static final int JUMP_TO_NEXT = 0X4;
    /**
     * 跳转到确认支付页面
     */
    public static final int JUMP_TO_CONFIRM = 0X5;


    /**
     * 0-退出，1-登录
     */
    public int  type;
    public Object[]  values;

    public ShopCartEvent(int type, Object...  value){
        this.type = type;
        this.values = value;
    }

    public ShopCartEvent(int type){
        this.type = type;
    }

}
