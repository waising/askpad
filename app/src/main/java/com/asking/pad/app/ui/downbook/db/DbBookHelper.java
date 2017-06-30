package com.asking.pad.app.ui.downbook.db;

import android.database.sqlite.SQLiteDatabase;

import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.entity.book.BookDownInfo;
import com.asking.pad.app.entity.book.BookTable;
import com.asking.pad.app.greendao.BookTableDao;
import com.asking.pad.app.greendao.DaoMaster;
import com.asking.pad.app.greendao.DaoSession;

import java.io.File;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by jswang on 2017/5/3.
 */

public class DbBookHelper {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /*单利对象*/
    private volatile static DbBookHelper INSTANCE;

    /**
     * 获取单例
     *
     * @return
     */
    public static DbBookHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (DbHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DbBookHelper();
                }
            }
        }
        return INSTANCE;
    }


    public DbBookHelper setDatabase(String commodityId) {
        try {
            String dbPath = BookDownInfo.getBookDirPath(commodityId);
            if (new File(dbPath).exists()) {
                mDaoMaster = new DaoMaster(SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE));
                mDaoSession = mDaoMaster.newSession();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public static void deleteDatabase(final String commodityId) {
        new Thread() {
            @Override
            public void run() {
                String dbPath = BookDownInfo.getBookDirPath(commodityId);
                FileUtils.deleteFile(new File(dbPath));
            }
        }.start();
    }

    private DaoSession getDaoSession() {
        return mDaoSession;
    }

    private BookTableDao getBookTableDao() {
        return getDaoSession().getBookTableDao();
    }

    public void getAllBookTable() {
        try {
            List<BookTable> list = getBookTableDao().queryBuilder().list();

            List<BookTable> ddd = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BookTable getBookTable(String pathId) {
        try {
            return getBookTableDao().queryBuilder().where(BookTableDao.Properties.PathId.eq(pathId)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
