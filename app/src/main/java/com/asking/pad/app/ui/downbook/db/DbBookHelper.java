package com.asking.pad.app.ui.downbook.db;

import android.app.Activity;
import android.database.Cursor;
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
import java.util.TreeMap;

/**
 * Created by jswang on 2017/5/3.
 */

public class DbBookHelper {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private SQLiteDatabase mDb;

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
            if (info != null && info.getDownState() == DownState.FINISH) {
                String dbPath = BookDownInfo.getBookDirPath(commodityId);
                if (new File(dbPath).exists()) {
                    mDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
                    mDaoMaster = new DaoMaster(mDb);
                    mDaoSession = mDaoMaster.newSession();
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public static void deleteBookDownInfoThread(final Activity mActivity, final BookDownInfo e, final ApiRequestListener mListener) {
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
            return getBookTableDao().load(pathId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * DbBookHelper.getInstance().setDatabase(gradeId).getBookTableValue(id);
//     * @param pathId
//     * @return
//     */
//
    public String getBookTableValue(String pathId) {
        try {
            Cursor cur =mDb.query(BookTableDao.TABLENAME,new String[]{BookTableDao.Properties.Value.columnName}
                ,BookTableDao.Properties.PathId.columnName+"=?"
                ,new String[]{pathId},null,null,null);
            if (cur.moveToFirst()){
                String value = cur.getString(0);
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TreeMap<Integer, byte[]> getBookTableVoicePath(String pathId) throws Exception {
//        Cursor cur =mDb.query(BookTableDao.TABLENAME,new String[]{BookTableDao.Properties.PathId.columnName}
//                ,BookTableDao.Properties.PathId.columnName+"=?"
//                ,new String[]{pathId},null,null,null);
        StringBuilder mBuilder = new StringBuilder("");

        TreeMap<Integer, byte[]> treeMap = new TreeMap();

        mDb.beginTransaction();
        Cursor cur = mDb.query(BookTableDao.TABLENAME, new String[]{BookTableDao.Properties.PathId.columnName, BookTableDao.Properties.Media.columnName}
                , BookTableDao.Properties.PathId.columnName + " LIKE ? ", new String[]{pathId + "%"}
                , null, null, null);
        try {
            for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext()){
                String id = cur.getString(0);

                Integer index = Integer.parseInt(id.replaceAll("^" + pathId + "_(.*)", "$1"));

                byte[] value = cur.getBlob(1);

                treeMap.put(index, value);
            }
        } finally {
            cur.close();
            mDb.endTransaction();
        }
        return treeMap;
    }
}
