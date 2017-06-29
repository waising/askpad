package com.asking.pad.app.entity.classex;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jswang on 2017/6/28.
 */

public class SubjectOption {

    @JSONField(name="_id")
    private String  optionsId;

    @JSONField(name="option_name")
    private String  optionName;

    @JSONField(name="option_content_html")
    private String  optionContentHtml;

    @JSONField(name="option_content")
    private String  optionContent;

    public boolean isSelect;

    public String getOptionsId() {
        return optionsId;
    }

    public void setOptionsId(String optionsId) {
        this.optionsId = optionsId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionContentHtml() {
        return optionContentHtml;
    }

    public void setOptionContentHtml(String optionContentHtml) {
        this.optionContentHtml = optionContentHtml;
    }

    public String getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }
}
