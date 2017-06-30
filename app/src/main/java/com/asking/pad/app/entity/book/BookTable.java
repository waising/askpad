package com.asking.pad.app.entity.book;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jswang on 2017/6/30.
 */

@Entity(nameInDb="sync_lesson")
public class BookTable {
    @Id
    @Property(nameInDb="k")
    private String pathId;

    @Property(nameInDb="v")
    private String value;

    @Generated(hash = 2086876761)
    public BookTable(String pathId, String value) {
        this.pathId = pathId;
        this.value = value;
    }

    @Generated(hash = 1470141119)
    public BookTable() {
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
