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

    @Property(nameInDb="b")
    private byte[] media;

    @Generated(hash = 170486571)
    public BookTable(String pathId, String value, byte[] media) {
        this.pathId = pathId;
        this.value = value;
        this.media = media;
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

    public byte[] getMedia() {
        return this.media;
    }

    public void setMedia(byte[] media) {
        this.media = media;
    }
}
