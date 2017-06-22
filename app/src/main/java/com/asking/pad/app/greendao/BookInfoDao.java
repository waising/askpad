package com.asking.pad.app.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.asking.pad.app.entity.BookInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOOK_INFO".
*/
public class BookInfoDao extends AbstractDao<BookInfo, String> {

    public static final String TABLENAME = "BOOK_INFO";

    /**
     * Properties of entity BookInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property BookId = new Property(0, String.class, "bookId", true, "BOOK_ID");
        public final static Property Url = new Property(1, String.class, "url", false, "URL");
        public final static Property VersionName = new Property(2, String.class, "versionName", false, "VERSION_NAME");
        public final static Property VersionId = new Property(3, String.class, "versionId", false, "VERSION_ID");
        public final static Property SubjectCatalogName = new Property(4, String.class, "subjectCatalogName", false, "SUBJECT_CATALOG_NAME");
        public final static Property SubjectCatalogCode = new Property(5, String.class, "subjectCatalogCode", false, "SUBJECT_CATALOG_CODE");
        public final static Property Version = new Property(6, String.class, "version", false, "VERSION");
        public final static Property Stage = new Property(7, String.class, "stage", false, "STAGE");
        public final static Property LevelId = new Property(8, String.class, "levelId", false, "LEVEL_ID");
        public final static Property LevelName = new Property(9, String.class, "levelName", false, "LEVEL_NAME");
        public final static Property UserId = new Property(10, String.class, "userId", false, "USER_ID");
        public final static Property DownState = new Property(11, byte.class, "downState", false, "DOWN_STATE");
        public final static Property CountLength = new Property(12, long.class, "countLength", false, "COUNT_LENGTH");
        public final static Property ReadLength = new Property(13, long.class, "readLength", false, "READ_LENGTH");
    }


    public BookInfoDao(DaoConfig config) {
        super(config);
    }
    
    public BookInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOK_INFO\" (" + //
                "\"BOOK_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: bookId
                "\"URL\" TEXT," + // 1: url
                "\"VERSION_NAME\" TEXT," + // 2: versionName
                "\"VERSION_ID\" TEXT," + // 3: versionId
                "\"SUBJECT_CATALOG_NAME\" TEXT," + // 4: subjectCatalogName
                "\"SUBJECT_CATALOG_CODE\" TEXT," + // 5: subjectCatalogCode
                "\"VERSION\" TEXT," + // 6: version
                "\"STAGE\" TEXT," + // 7: stage
                "\"LEVEL_ID\" TEXT," + // 8: levelId
                "\"LEVEL_NAME\" TEXT," + // 9: levelName
                "\"USER_ID\" TEXT," + // 10: userId
                "\"DOWN_STATE\" INTEGER NOT NULL ," + // 11: downState
                "\"COUNT_LENGTH\" INTEGER NOT NULL ," + // 12: countLength
                "\"READ_LENGTH\" INTEGER NOT NULL );"); // 13: readLength
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOK_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BookInfo entity) {
        stmt.clearBindings();
 
        String bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindString(1, bookId);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
 
        String versionName = entity.getVersionName();
        if (versionName != null) {
            stmt.bindString(3, versionName);
        }
 
        String versionId = entity.getVersionId();
        if (versionId != null) {
            stmt.bindString(4, versionId);
        }
 
        String subjectCatalogName = entity.getSubjectCatalogName();
        if (subjectCatalogName != null) {
            stmt.bindString(5, subjectCatalogName);
        }
 
        String subjectCatalogCode = entity.getSubjectCatalogCode();
        if (subjectCatalogCode != null) {
            stmt.bindString(6, subjectCatalogCode);
        }
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(7, version);
        }
 
        String stage = entity.getStage();
        if (stage != null) {
            stmt.bindString(8, stage);
        }
 
        String levelId = entity.getLevelId();
        if (levelId != null) {
            stmt.bindString(9, levelId);
        }
 
        String levelName = entity.getLevelName();
        if (levelName != null) {
            stmt.bindString(10, levelName);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(11, userId);
        }
        stmt.bindLong(12, entity.getDownState());
        stmt.bindLong(13, entity.getCountLength());
        stmt.bindLong(14, entity.getReadLength());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BookInfo entity) {
        stmt.clearBindings();
 
        String bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindString(1, bookId);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
 
        String versionName = entity.getVersionName();
        if (versionName != null) {
            stmt.bindString(3, versionName);
        }
 
        String versionId = entity.getVersionId();
        if (versionId != null) {
            stmt.bindString(4, versionId);
        }
 
        String subjectCatalogName = entity.getSubjectCatalogName();
        if (subjectCatalogName != null) {
            stmt.bindString(5, subjectCatalogName);
        }
 
        String subjectCatalogCode = entity.getSubjectCatalogCode();
        if (subjectCatalogCode != null) {
            stmt.bindString(6, subjectCatalogCode);
        }
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(7, version);
        }
 
        String stage = entity.getStage();
        if (stage != null) {
            stmt.bindString(8, stage);
        }
 
        String levelId = entity.getLevelId();
        if (levelId != null) {
            stmt.bindString(9, levelId);
        }
 
        String levelName = entity.getLevelName();
        if (levelName != null) {
            stmt.bindString(10, levelName);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(11, userId);
        }
        stmt.bindLong(12, entity.getDownState());
        stmt.bindLong(13, entity.getCountLength());
        stmt.bindLong(14, entity.getReadLength());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public BookInfo readEntity(Cursor cursor, int offset) {
        BookInfo entity = new BookInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // bookId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // url
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // versionName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // versionId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // subjectCatalogName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // subjectCatalogCode
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // version
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // stage
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // levelId
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // levelName
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // userId
            (byte) cursor.getShort(offset + 11), // downState
            cursor.getLong(offset + 12), // countLength
            cursor.getLong(offset + 13) // readLength
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BookInfo entity, int offset) {
        entity.setBookId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUrl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setVersionName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setVersionId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSubjectCatalogName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSubjectCatalogCode(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setVersion(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setStage(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLevelId(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLevelName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setUserId(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setDownState((byte) cursor.getShort(offset + 11));
        entity.setCountLength(cursor.getLong(offset + 12));
        entity.setReadLength(cursor.getLong(offset + 13));
     }
    
    @Override
    protected final String updateKeyAfterInsert(BookInfo entity, long rowId) {
        return entity.getBookId();
    }
    
    @Override
    public String getKey(BookInfo entity) {
        if(entity != null) {
            return entity.getBookId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BookInfo entity) {
        return entity.getBookId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}