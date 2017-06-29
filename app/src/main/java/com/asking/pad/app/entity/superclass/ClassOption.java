package com.asking.pad.app.entity.superclass;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jswang on 2017/6/28.
 */

public class ClassOption{
    @JSONField(name = "option_name")
    public String optionName;

    @JSONField(name = "option_content_html")
    public String optionContentHtml;
}