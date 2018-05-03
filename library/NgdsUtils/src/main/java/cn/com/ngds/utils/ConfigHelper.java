package cn.com.ngds.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置项管理类
 */
public class ConfigHelper {

    private static String PREF_NAME = "Event_Preferences";
    private static ConfigHelper mInstance = null;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;

    public static ConfigHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ConfigHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public String loadKey(String key) {
        return mSettings.getString(key, "");
    }

    public void saveKey(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void removeKey(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    public void clearkeys() {
        mEditor.clear();
        mEditor.commit();
    }

    public boolean loadBooleanKey(String key, boolean defValue) {
        return mSettings.getBoolean(key, defValue);
    }

    public void saveBooleanKey(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public int loadIntKey(String key, int defValue) {
        return mSettings.getInt(key, defValue);
    }

    public void saveIntKey(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public long loadLongKey(String key, long defValue) {
        return mSettings.getLong(key, defValue);
    }

    public void saveLongKey(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    private ConfigHelper(Context c) {
        mSettings = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    public boolean containsKey(String key) {
        return mSettings.contains(key);
    }
}
