package com.asking.pad.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wxwang on 2017/7/18.
 */

public class QuestionEntity implements Parcelable {

    private String id;
    private String userId;
    private String userName;
    private String title;
    private String description;
    private String km;

    public List<AnwserMoreEntity> getList() {
        return list;
    }

    public void setList(List<AnwserMoreEntity> list) {
        this.list = list;
    }

    private List<AnwserMoreEntity> list;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCaifu() {
        return caifu;
    }

    public void setCaifu(int caifu) {
        this.caifu = caifu;
    }


    private String levelId;

    public String getCreateDate_Fmt() {
        return createDate_Fmt;
    }

    public void setCreateDate_Fmt(String createDate_Fmt) {
        this.createDate_Fmt = createDate_Fmt;
    }

    public int getAnswer_size() {
        return answer_size;
    }

    public void setAnswer_size(int answer_size) {
        this.answer_size = answer_size;
    }

    private String createDate_Fmt;
    private String state;
    private int caifu;

    private int answer_size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QuestionEntity() {
    }


    public static class AnwserMoreEntity implements Parcelable {
        private String createDate_fmt;
        private long createDate;
        private int lousySize;

        public String getCreateDate_fmt() {
            return createDate_fmt;
        }

        public void setCreateDate_fmt(String createDate_fmt) {
            this.createDate_fmt = createDate_fmt;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public int getLousySize() {
            return lousySize;
        }

        public void setLousySize(int lousySize) {
            this.lousySize = lousySize;
        }

        public List<AnswerDetail> getList() {
            return list;
        }

        public void setList(List<AnswerDetail> list) {
            this.list = list;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isAdopt() {
            return adopt;
        }

        public void setAdopt(boolean adopt) {
            this.adopt = adopt;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getGoodSize() {
            return goodSize;
        }

        public void setGoodSize(int goodSize) {
            this.goodSize = goodSize;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        private List<AnswerDetail> list;
        private String userId;
        private String id;
        private boolean adopt;
        private String userName;
        private String content;
        private int goodSize;
        private int type;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.createDate_fmt);
            dest.writeLong(this.createDate);
            dest.writeInt(this.lousySize);
            dest.writeList(this.list);
            dest.writeString(this.userId);
            dest.writeString(this.id);
            dest.writeByte(this.adopt ? (byte) 1 : (byte) 0);
            dest.writeString(this.userName);
            dest.writeString(this.content);
            dest.writeInt(this.goodSize);
            dest.writeInt(this.type);
        }

        public AnwserMoreEntity() {
        }

        protected AnwserMoreEntity(Parcel in) {
            this.createDate_fmt = in.readString();
            this.createDate = in.readLong();
            this.lousySize = in.readInt();
            this.list = new ArrayList<AnswerDetail>();
            in.readList(this.list, AnswerDetail.class.getClassLoader());
            this.userId = in.readString();
            this.id = in.readString();
            this.adopt = in.readByte() != 0;
            this.userName = in.readString();
            this.content = in.readString();
            this.goodSize = in.readInt();
            this.type = in.readInt();
        }

        public static final Creator<AnwserMoreEntity> CREATOR = new Creator<AnwserMoreEntity>() {
            @Override
            public AnwserMoreEntity createFromParcel(Parcel source) {
                return new AnwserMoreEntity(source);
            }

            @Override
            public AnwserMoreEntity[] newArray(int size) {
                return new AnwserMoreEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.km);
        dest.writeList(this.list);
        dest.writeString(this.levelId);
        dest.writeString(this.createDate_Fmt);
        dest.writeString(this.state);
        dest.writeInt(this.caifu);
        dest.writeInt(this.answer_size);
    }

    protected QuestionEntity(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.userName = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.km = in.readString();
        this.list = new ArrayList<AnwserMoreEntity>();
        in.readList(this.list, AnwserMoreEntity.class.getClassLoader());
        this.levelId = in.readString();
        this.createDate_Fmt = in.readString();
        this.state = in.readString();
        this.caifu = in.readInt();
        this.answer_size = in.readInt();
    }

    public static final Creator<QuestionEntity> CREATOR = new Creator<QuestionEntity>() {
        @Override
        public QuestionEntity createFromParcel(Parcel source) {
            return new QuestionEntity(source);
        }

        @Override
        public QuestionEntity[] newArray(int size) {
            return new QuestionEntity[size];
        }
    };

    public class AnswerDetail{
        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public long getAnswerTime() {
            return answerTime;
        }

        public void setAnswerTime(long answerTime) {
            this.answerTime = answerTime;
        }

        private String answer;
        private long answerTime;

        private String ask;

        public String getAsk() {
            return ask;
        }

        public void setAsk(String ask) {
            this.ask = ask;
        }

        public long getAskTime() {
            return askTime;
        }

        public void setAskTime(long askTime) {
            this.askTime = askTime;
        }

        private long askTime;
    }
}