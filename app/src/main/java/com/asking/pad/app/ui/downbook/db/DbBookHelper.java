package com.asking.pad.app.ui.downbook.db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.entity.book.BookDownInfo;
import com.asking.pad.app.entity.book.BookTable;
import com.asking.pad.app.greendao.BookTableDao;
import com.asking.pad.app.greendao.DaoMaster;
import com.asking.pad.app.greendao.DaoSession;
import com.asking.pad.app.ui.downbook.download.DownState;
import com.asking.pad.app.ui.downbook.download.OkHttpDownManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
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
//    public String getBookTableValue(String pathId) {
//        try {
//            String sql = String.format("SELECT * FROM sync_lesson  WHERE k = '%s'",pathId);
//            Cursor cur = getBookTableDao().getDatabase().rawQuery( sql, null);
//            if (cur.moveToFirst()){
//                String columnName = BookTableDao.Properties.PathId.columnName;
//                int nameColumnIndex = cur.getColumnIndex(columnName);
//                String name = cur.getString(nameColumnIndex);
//
//                String columnValue = BookTableDao.Properties.Value.columnName;
//                int valueColumnIndex = cur.getColumnIndex(columnValue);
//                String value = cur.getString(valueColumnIndex);
//
//                return value;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public void insertBookTableValue() throws Exception {
        try {
            //mDb
            File file = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_BOOK_PATH + "/voice/1.mp3");
            if (file.exists()) {
                int byte_size = 1024;
                byte[] b = new byte[byte_size];
                FileInputStream fileInputStream = new FileInputStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                        byte_size);
                for (int length; (length = fileInputStream.read(b)) != -1; ) {
                    outputStream.write(b, 0, length);
                }
                fileInputStream.close();
                outputStream.close();
                String ddd = new String(outputStream.toByteArray());

                ContentValues cv = new ContentValues();
                cv.put(BookTableDao.Properties.PathId.columnName, "1234567");
                cv.put(BookTableDao.Properties.Value.columnName, ddd);
                mDb.insert(BookTableDao.TABLENAME, null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBookTableValue(String pathId) throws Exception {
//        Cursor cur =mDb.query(BookTableDao.TABLENAME,new String[]{BookTableDao.Properties.PathId.columnName}
//                ,BookTableDao.Properties.PathId.columnName+"=?"
//                ,new String[]{pathId},null,null,null);
        StringBuilder mBuilder = new StringBuilder("");

        TreeMap<Integer, String> treeMap = new TreeMap();

        mDb.beginTransaction();
        Cursor cur = mDb.query(BookTableDao.TABLENAME, new String[]{BookTableDao.Properties.PathId.columnName, BookTableDao.Properties.Value.columnName}
                , BookTableDao.Properties.PathId.columnName + " LIKE ? ", new String[]{pathId + "%"}
                , null, null, null);

        try {
            for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext()){
                String id = cur.getString(0);

                Integer index = Integer.parseInt(id.replaceAll("^" + pathId + "_(.*)", "$1"));

                String value = cur.getString(1);

                treeMap.put(index, value);
            }
            System.out.println(treeMap.keySet());
            for (Map.Entry<Integer, String> integerStringEntry : treeMap.entrySet()) {
                mBuilder.append(integerStringEntry.getValue());
            }

        } finally {
            cur.close();
            mDb.endTransaction();
        }
        return mBuilder.toString();
    }
}
