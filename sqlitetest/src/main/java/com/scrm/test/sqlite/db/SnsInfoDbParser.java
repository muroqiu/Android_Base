package com.scrm.test.sqlite.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 手柄连接信息DB解析类
 * 
 * @author 慕容秋 (muroqiu@qq.com)
 */
public class SnsInfoDbParser implements DbParser<SnsInfo> {
    public static final String USER_NAME = "userName";
    public static final String CONTENT = "content";
    public static final String ATTRBUF = "attrBuf";

    @Override
    public SnsInfo parse(Cursor dbCursor) {
        SnsInfo obj = new SnsInfo();

        obj.setUserName(dbCursor.getString(dbCursor.getColumnIndex(USER_NAME)));
        obj.setContent(dbCursor.getBlob(dbCursor.getColumnIndex(CONTENT)));
        obj.setAttr(dbCursor.getBlob(dbCursor.getColumnIndex(ATTRBUF)));

        return obj;
    }

    @Override
    public ContentValues toContentValues(SnsInfo t) {
        ContentValues values = new ContentValues();
        values.put(USER_NAME, t.getUserName());
        values.put(CONTENT, t.getContent());
        values.put(ATTRBUF, t.getAttr());

        return values;
    }
}
