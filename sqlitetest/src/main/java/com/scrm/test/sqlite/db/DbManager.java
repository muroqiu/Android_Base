package com.scrm.test.sqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 数据相关业务封装
 *
 * @author 慕容秋 (muroqiu@qq.com)
 */
public final class DbManager {
    private Context mContext;

    private SQLiteDatabase mDb;

    private static DbManager dbManager = null;

    private final static String sampleDb = "ngds_sample.db3";

    public static final String DATABASE_NAME_SUFFIX = "ngds_2015";

    private static String mDbName = "";

    private static String mUid = "";

    // 游戏运行数据
    public static final String TABLE_GAMETIMES = "GameTimes";
    // 手柄连接数据
    public static final String TABLE_PADCONNECTS = "PadConnects";


    public static final String TABLE_SNSINFO = "SnsInfo";

    // 日志线程
    private Timer mWriteTimer = new Timer();

    /**
     * 获取 DbManager 实例 <br>
     * Created 2014年9月16日 上午12:14:39
     *
     * @param context 上下文句柄
     * @param uid     用户ID
     * @return DbManager
     * @author wat.ztag
     */
    public static DbManager getInstance(Context context, String uid) {
        if (dbManager == null || !mUid.equals(uid)) {
            dbManager = new DbManager(context, uid);
        }

        return dbManager;
    }

    /**
     * Creates a new instance of DbManager. <br>
     * Created 2014年9月16日 上午12:14:44
     *
     * @param context 上下文句柄
     * @param uid     用户ID
     */
    private DbManager(Context context, String uid) {
        closeDB();
        mUid = uid;
        mContext = context.getApplicationContext();
//        mDbName = DATABASE_NAME_SUFFIX + uid + ".db3";
//        createDb(mDbName);

        mDbName = "/sdcard/SnsMicroMsg.db";
        Log.e("DB", mDbName);

        PadDatabase helper = new PadDatabase(context, mDbName);
        mDb = helper.getWritableDatabase();
    }

    /**
     * 创建数据库 <br>
     * Created 2014年9月16日 上午12:14:48
     *
     * @param ADBName 数据库名称
     * @author wat.ztag
     */
    private void createDb(String ADBName) {
        File Afile = mContext.getDatabasePath(ADBName);
        if (!Afile.exists()) {
            try {
                Afile.getParentFile().mkdirs();
                int byteread = 0;
                InputStream inStream = mContext.getAssets().open(sampleDb);// 载模板
                //                InputStream inStream = mContext.getResources().openRawResource(R.raw.mbx_sample);// 载模板
                FileOutputStream fs = new FileOutputStream(Afile.getPath());
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            } catch (Exception e) {
                // System.out.println("复制单个文件操作出错");
                e.printStackTrace();
//                Log.d(e.getMessage());
            }
        }
    }

    /**
     * 关闭数据库 <br>
     * Created 2014年9月16日 上午12:14:51
     *
     * @author wat.ztag
     */
    public void closeDB() {
        if (mDb != null && mDb.isOpen()) {
            mDb.close();
        }
    }

    /**
     * 关闭数据库游标 <br>
     * Created 2014年9月16日 上午12:14:54
     *
     * @param cursor 游标
     * @author wat.ztag
     */
    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * 增加游戏运行数据
     *
     * @param data
     */
    public void addGameTimes(final LogGameTimes data) {
        mWriteTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
//                    ContentValues values = new LogGameTimesDbParser().toContentValues(data);
//                    mDb.replace(TABLE_GAMETIMES, null, values);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, 10);
    }

    /**
     * 删除历史游戏数据
     *
     * @param time
     */

    public void delGameTimes(long time) {
        try {
//            mDb.delete(TABLE_GAMETIMES, LogGameTimesDbParser.LOG_TIME + " <= " + time, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取游戏运行数据记录
     *
     * @return
     */
//    public List<LogGameTimes> getGameTimesList() {
//        List<LogGameTimes> result = new ArrayList<LogGameTimes>();
//        String sql =
//            "SELECT * FROM " + TABLE_GAMETIMES + " ORDER BY " + LogGameTimesDbParser.LOG_TIME
//                + " ASC LIMIT 100";
//        try {
//            Cursor cursor = mDb.rawQuery(sql, null);
//            while (cursor.moveToNext()) {
//                LogGameTimes obj = new LogGameTimesDbParser().parse(cursor);
//                result.add(obj);
//            }
//            closeCursor(cursor);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /**
     * 执行SQL <br>
     * Created 2014年9月16日 上午12:16:04
     *
     * @param sql sql语句
     * @author wat.ztag
     */
    private void execSql(String sql) {
//        Log.d(sql);
        mDb.execSQL(sql);
    }


    /**
     * 增加游戏运行数据
     *
     * @param data
     */
    public void addPadConnects(final LogPadConnects data) {
        mWriteTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    ContentValues values = new LogPadConnectsDbParser().toContentValues(data);
                    mDb.replace(TABLE_PADCONNECTS, null, values);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, 10);
    }

    /**
     * 删除历史游戏数据
     *
     * @param time
     */

    public void delPadConnects(long time) {
        try {
            mDb.delete(TABLE_PADCONNECTS, LogPadConnectsDbParser.LOG_TIME + " <= " + time, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取游戏运行数据记录
     *
     * @return
     */
    public List<LogPadConnects> getPadConnectsList() {
        List<LogPadConnects> result = new ArrayList<LogPadConnects>();
        String sql =
                "SELECT * FROM " + TABLE_PADCONNECTS + " ORDER BY " + LogPadConnectsDbParser.LOG_TIME
                        + " ASC LIMIT 100";
        try {
            Cursor cursor = mDb.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                LogPadConnects obj = new LogPadConnectsDbParser().parse(cursor);
                result.add(obj);
            }
            closeCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取游戏运行数据记录
     *
     * @return
     */
    public List<SnsInfo> getSnsInfoList() {
        List<SnsInfo> result = new ArrayList<SnsInfo>();
        String sql =
                "SELECT * FROM " + TABLE_SNSINFO + " LIMIT 2";
        try {
            Cursor cursor = mDb.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                SnsInfo obj = new SnsInfoDbParser().parse(cursor);
                result.add(obj);
            }
            closeCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
