package com.scrm.test.sqlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 库表封装
 * 
 * @author 慕容秋 (muroqiu@qq.com)
 */
public class PadDatabase extends SQLiteOpenHelper {
    // 版本 1:初始版本； 10:加了PadConnects表的版本
    private static final int VERSION = 10;

    /**
     * Creates a new instance of SQLiteDataHelper. <br>
     * Created 2014年9月15日 下午11:25:14
     * 
     * @param context
     *            上下文句柄
     * @param dbName
     *            数据库名称
     */
    public PadDatabase(Context context, String dbName) {
        super(context, dbName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 10) {
            // 增加PadConnects表
//            try {
//                db.execSQL("CREATE TABLE \"PadConnects\" (\"LogTime\" long PRIMARY KEY  NOT NULL  DEFAULT (0) ,\"Sign\" VARCHAR,\"Extra1\" VARCHAR,\"Extra2\" VARCHAR,\"Extra3\" VARCHAR)");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }
    }

}
