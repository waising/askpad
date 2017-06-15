package com.asking.pad.app.ui.downbook.db;

import android.database.sqlite.SQLiteDatabase;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.entity.BookInfo;
import com.asking.pad.app.entity.classmedia.ClassMediaTable;
import com.asking.pad.app.entity.classmedia.StudyRecord;
import com.asking.pad.app.greendao.BookInfoDao;
import com.asking.pad.app.greendao.ClassMediaTableDao;
import com.asking.pad.app.greendao.DaoMaster;
import com.asking.pad.app.greendao.DaoSession;
import com.asking.pad.app.greendao.StudyRecordDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/5/3.
 */

public class DbHelper {
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /*单利对象*/
    private volatile static DbHelper INSTANCE;

    /**
     * 获取单例
     *
     * @return
     */
    public static DbHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (DbHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DbHelper();
                    INSTANCE.setDatabase();
                }
            }
        }
        return INSTANCE;
    }

    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(AppContext.getInstance(), "notes-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    private DaoSession getDaoSession() {
        return mDaoSession;
    }


//------------------------超级辅导课下载---------------------------------------

    private BookInfoDao getBookInfoDao() {
        return getDaoSession().getBookInfoDao();
    }

    public void insertBookInfo(BookInfo e) {
        getBookInfoDao().insert(e);
    }

    public void insertOrReplaceBookInfo(BookInfo e) {
        getBookInfoDao().insertOrReplace(e);
    }

    public void updateBookInfo(BookInfo e) {
        getBookInfoDao().update(e);
    }

    public void deleteByIdBookInfo(String id) {
        getBookInfoDao().deleteByKey(id);
    }

    public List<BookInfo> getAllBookInfo() {
        return getBookInfoDao().queryBuilder().where(BookInfoDao.Properties.UserId.eq(AppContext.getInstance().getUserId())).list();
    }

//------------------------超级辅导课下载---------------------------------------

    private ClassMediaTableDao getClassMediaTableDao() {
        return getDaoSession().getClassMediaTableDao();
    }

    public void insertOrReplaceClassMedia(ClassMediaTable e) {
        getClassMediaTableDao().insertOrReplace(e);
    }

    public void updateClassMedia(ClassMediaTable e) {
        getClassMediaTableDao().update(e);
    }

    public List<ClassMediaTable> getAllClassMedia() {
        return getClassMediaTableDao().queryBuilder().where(ClassMediaTableDao.Properties.UserId.eq(AppContext.getInstance().getUserId())).list();
    }


//------------------------初升高播发进度---------------------------------------

    private StudyRecordDao getStudyRecordDao() {
        return getDaoSession().getStudyRecordDao();
    }

    public void insertOrReplaceStudyRecord(String id, int max, int progress) {
        try {
            StudyRecord e = new StudyRecord();
            e.setCourseDataId(id);
            e.setPlayMax(max);
            e.setPlayProgress(progress);
            int i2 = Math.round(100.0f * progress / max);
            e.setPlayPercentage(i2);
            getStudyRecordDao().insertOrReplace(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<StudyRecord> getStudyRecordList() {
        List<StudyRecord> mRecordList = new ArrayList<>();
        List<StudyRecord> list = getStudyRecordDao().queryBuilder().list();
        if (list != null && list.size() > 0) {
            mRecordList.addAll(list);
        }
        return mRecordList;
    }

}
