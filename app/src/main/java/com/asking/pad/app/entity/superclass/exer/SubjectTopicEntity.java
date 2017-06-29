package com.asking.pad.app.entity.superclass.exer;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jswang on 2017/6/29.
 */

public class SubjectTopicEntity {
    @JSONField(name = "topic_id")
    public String topicId;

    @JSONField(name = "topic_name")
    public String topicName;

    public boolean isSelect;

    public SubjectTopicEntity() {

    }

    public SubjectTopicEntity(String topicId, String topicName) {
        this.topicId = topicId;
        this.topicName = topicName;
    }
}
