package com.asking.pad.app.ui.downbook.db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.entity.book.BookDownInfo;
import com.asking.pad.app.entity.book.BookTable;
import com.asking.pad.app.greendao.BookTableDao;
import com.asking.pad.app.greendao.DaoMaster;
import com.asking.pad.app.greendao.DaoSession;
import com.asking.pad.app.ui.downbook.download.DownState;
import com.asking.pad.app.ui.downbook.download.OkHttpDownManager;

import java.io.File;
import java.util.List;

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
            BookDownInfo info = DbHelper.getInstance().getBookDownInfo(commodityId);
            if(info!=null && info.getDownState() == DownState.FINISH){
                String dbPath = BookDownInfo.getBookDirPath(commodityId);
                if (new File(dbPath).exists()) {
                    mDaoMaster = new DaoMaster(SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE));
                    mDaoSession = mDaoMaster.newSession();
                }
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public static void deleteBookDownInfoThread(final Activity mActivity,final BookDownInfo e,final ApiRequestListener mListener) {
        new Thread() {
            @Override
            public void run() {
                OkHttpDownManager.getInstance().deleteDown(e);
                String dbPath = BookDownInfo.getBookDirPath(e.getCommodityId());
                FileUtils.deleteFile(new File(dbPath));
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onResultSuccess("");
                    }
                });
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
