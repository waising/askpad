package com.asking.pad.app.ui.downbook.db;

import android.database.sqlite.SQLiteDatabase;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.entity.book.BookDownInfo;
import com.asking.pad.app.entity.classmedia.ClassMediaTable;
import com.asking.pad.app.entity.classmedia.StudyRecord;
import com.asking.pad.app.greendao.BookDownInfoDao;
import com.asking.pad.app.greendao.ClassMediaTableDao;
import com.asking.pad.app.greendao.DaoMaster;
import com.asking.pad.app.greendao.DaoSession;
import com.asking.pad.app.greendao.StudyRecordDao;
import com.asking.pad.app.ui.downbook.download.OkHttpDownManager;

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

    private BookDownInfoDao getBookInfoDao() {
        return getDaoSession().getBookDownInfoDao();
    }

    public void insertBookInfo(BookDownInfo e) {
        getBookInfoDao().insert(e);
    }

    public void insertOrReplaceBookInfo(BookDownInfo e) {
        getBookInfoDao().insertOrReplace(e);
    }

    public void updateBookInfo(BookDownInfo e) {
        getBookInfoDao().update(e);
    }

    public void deleteByIdBookInfo(String id) {
        getBookInfoDao().deleteByKey(id);
    }

    public BookDownInfo getBookDownInfo(String id) {
      return   getBookInfoDao().load(id);
    }

    public List<BookDownInfo> getAllBookDownInfo(String CourseTypeId) {
        List<BookDownInfo> dbList = getBookInfoDao().queryBuilder().where(BookDownInfoDao.Properties.UserId.eq(AppContext.getInstance().getUserId())
                , BookDownInfoDao.Properties.CourseTypeId.eq(CourseTypeId)).list();
        for(int j= 0;j<dbList.size();j++){
            BookDownInfo dbE = dbList.get(j);
            if(!FileUtils.isFileExists(dbE.getSavePath())){
                OkHttpDownManager.getInstance().deleteDown(dbE);
            }
        }
        return dbList;
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

    public void deleteByIdClassMedia(String id) {
        getClassMediaTableDao().deleteByKey(id);
    }


//------------------------初升高播发进度---------------------------------------

    private StudyRecordDao getStudyRecordDao() {
        return getDaoSession().getStudyRecordDao();
    }

    public void insertOrReplaceStudyRecord(StudyRecord e) {
        try {
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

    public int getPlayProgress(String courseDataId) {
        try {
            StudyRecord e = getStudyRecordDao().queryBuilder().where(StudyRecordDao.Properties.CourseDataId.eq(courseDataId)).unique();
            if (e != null) {
                return e.getPlayProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
