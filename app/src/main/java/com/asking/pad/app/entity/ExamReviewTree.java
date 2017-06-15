package com.asking.pad.app.entity;

import java.util.ArrayList;

/**
 * Created by jswang on 2017/4/11.
 */

public class ExamReviewTree {
    public String _id;
    private String index;
    private String text;

    public String id;
    public String name;

    public boolean isExpand = false;


    public ArrayList<ExamReviewTree> list = new ArrayList<>();

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public ExamReviewTree() {
    }
}
