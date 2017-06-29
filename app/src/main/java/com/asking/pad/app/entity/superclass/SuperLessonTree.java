package com.asking.pad.app.entity.superclass;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class SuperLessonTree {

    @JSONField(name="id")
    public String id;

    @JSONField(name="name")
    public String name;

    @JSONField(name="free")
    public int free;

    @JSONField(name="purchased")
    public int purchased;

    @JSONField(name="children")
    public List<SuperLessonTree> children = new ArrayList<>();

    public int knowledgeIndex;

    public boolean getIsLeaf(){
        return !(children != null && children.size() > 0);
    }

}
