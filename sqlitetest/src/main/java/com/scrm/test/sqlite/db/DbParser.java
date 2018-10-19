package com.scrm.test.sqlite.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * <br>
 * Created 2014年9月15日 下午11:21:55
 *
 * @param <T>
 * @author wat.ztag
 * @see @param <T>
 */
public interface DbParser<T extends BaseType> {
    /**
     * <br>
     * Created 2014年9月15日 下午11:21:59
     *
     * @param dbCursor Cursor
     * @return T
     * @author wat.ztag
     */
    T parse(Cursor dbCursor);

    /**
     * <br>
     * Created 2014年9月15日 下午11:21:46
     *
     * @param t T
     * @return ContentValues
     * @author wat.ztag
     */
    ContentValues toContentValues(T t);

}
