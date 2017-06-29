package com.asking.pad.app.entity.pay;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

/**
 * Created by jswang on 2017/6/29.
 */

public class AskCoinPay {
    @JSONField(name = "_id")
    public String _id;

    @JSONField(name = "value")
    public String value;

    @JSONField(name = "commodityId")
    public String commodityId;

    @JSONField(name = "price")
    public String price;

    @JSONField(name = "askCurrencyId")
    public String askCurrencyId;

    public boolean isSelect;

    public double getAskCoinPrice(){
        try{
            BigDecimal p = new BigDecimal(price);
            return  p.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
