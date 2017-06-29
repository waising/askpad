package com.asking.pad.app.entity.classex;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jswang on 2017/6/28.
 */

public class SubjectType {

    @JSONField(name="type_id")
    private String  typeId;

    @JSONField(name="type_name")
    private String  typeName;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
