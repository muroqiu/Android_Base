package com.scrm.test.sqlite.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 手柄连接信息DB解析类
 * 
 * @author 慕容秋 (muroqiu@qq.com)
 */
public class LogPadConnectsDbParser implements DbParser<LogPadConnects> {
    public static final String LOG_TIME = "LogTime";
    public static final String SIGN = "Sign";
    public static final String UUID = "Extra1";

    @Override
    public LogPadConnects parse(Cursor dbCursor) {
        LogPadConnects obj = new LogPadConnects();

        obj.setLogTime(dbCursor.getLong(dbCursor.getColumnIndex(LOG_TIME)));
        obj.setSign(dbCursor.getString(dbCursor.getColumnIndex(SIGN)));
        obj.setUUID(dbCursor.getString(dbCursor.getColumnIndex(UUID)));

        return obj;
    }

    @Override
    public ContentValues toContentValues(LogPadConnects t) {
        ContentValues values = new ContentValues();
        values.put(LOG_TIME, t.getLogTime());
        values.put(SIGN, t.getSign());
        values.put(UUID, t.getUUID());

        return values;
    }
}
