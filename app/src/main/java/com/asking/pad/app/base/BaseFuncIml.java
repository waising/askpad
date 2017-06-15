package com.asking.pad.app.base;

/**
 * Created by jswang on 2017/4/7.
 */

public interface BaseFuncIml {
    /** 初始化数据方法 */
    void initData();

    /** 初始化UI控件方法 */
    void initView();

    /** 初始化事件监听方法 */
    void initListener();

    /** 初始化界面加载 */
    void initLoad();
}
