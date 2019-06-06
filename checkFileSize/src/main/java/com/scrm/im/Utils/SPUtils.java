package com.scrm.im.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 共享首选项工具类，用于统一管理应用的全部共享首选项，使用方式如下：
 * <p>
 * 1、将共享首选项文件名（大写、下划线分隔）添加到内部枚举{@link SpType}中；
 * <p>
 * 2、使用时先调用{@link #getSp}获取{@link Sp}的实例，然后再调用对应的读写接口。
 *
 * @author Reinhard（李剑波）
 * @date 2018/7/3
 */
@SuppressWarnings("unused")
public class SPUtils {
    /**
     * 共享首选项映射表
     */
    private static final Map<SpType, Sp> SP_MAP = new HashMap<>();

    public static Sp getSp() {
        return getSp(SpType.DEFAULT);
    }

    // 最大缓存数据量
    private static final int MAX_CACHE = 3000;

    // 缓存过期时间(48小时)：秒
    private static final int TIME_CACHE = 60;

    public static Sp getSp(SpType spType) {
        if (spType == null) {
            spType = SpType.DEFAULT;
        }

        Sp sp = SP_MAP.get(spType);
        if (sp == null) {
            synchronized (SP_MAP) {
                if (SP_MAP.get(spType) == null) {
                    try {
                        // 共享首选项的文件名使用枚举类型的名称（小写）
                        sp = new Sp(AppUtils.getApp(), spType.name().toLowerCase(), spType.getMode());
                        // 上传、下载需清理过期缓存数据
                        if (spType == SpType.FILE_DOWNLOAD_CACHER || spType == SpType.FILE_UPLOAD_CACHER) {
                            clearOverdue(sp);
                        }
                        SP_MAP.put(spType, sp);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return sp;
    }


    /**
     * 超过数量限制时删除过期缓存数据
     * @param sp
     */
    private static void clearOverdue(Sp sp) {
        Map<String, ?> all = sp.getAll();
        Log.e("TAG", all.size() + "");
        int dataSize = all.size();
        if (all != null && dataSize > MAX_CACHE) {
            Set<String> keySet = all.keySet();
            SharedPreferences.Editor editor = sp.batchEditor();
            for (String key : keySet) {
                try {
                    Log.e("TAG", key);
                    UriCacher uriCacher = new UriCacher((String) all.get(key));

                    if (dataSize > MAX_CACHE / 10 && uriCacher.getTimes() + TIME_CACHE < TimeUtils.seconds()) {
                        Log.e("TAG", sp.getString(key, ""));
                        editor.remove(key);
                        dataSize --;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            editor.apply();
        }
    }

    /**
     * 共享首选项类型，一个共享首选项文件名对应一个枚举类型
     */
    public enum SpType {
        /**
         * 默认
         */
        DEFAULT(Context.MODE_PRIVATE),
        /**
         * 腾讯云上普通文件相关
         */
        COS_NORMAL(Context.MODE_PRIVATE),

        /**
         * 腾讯云上Log文件相关
         */
        COS_LOG(Context.MODE_PRIVATE),

        /**
         * 打点：微信进程状态、IM进程状态、IM连接状态、设备网络状态
         */
        DOT(Context.MODE_PRIVATE),

        /**
         * APP更新检查，比对当前时间和上次检查更新的时间，超过UpdateChecker.CHECK_UPDATE_INTERVAL，则进行更新检查
         */
        APP_UPDATE(Context.MODE_PRIVATE),

        /**
         * 保存服务端访问的token
         */
        TOKEN(Context.MODE_PRIVATE),

        /**
         * URL配置相关，存储从本地文件读取到的url
         */
        URL_CONFIG(Context.MODE_PRIVATE),

        /**
         * 上传文件缓存，存储格式   url:{url, md5, seconds}
         */
        FILE_UPLOAD_CACHER(Context.MODE_PRIVATE),

        /**
         * 下载文件缓存，存储格式   url:{filePath, md5}
         */
        FILE_DOWNLOAD_CACHER(Context.MODE_PRIVATE);

        /**
         * 操作模式，对应{@link android.content.Context#getSharedPreferences}传入的mode
         */
        int mode;

        SpType(int mode) {
            this.mode = mode;
        }

        int getMode() {
            return this.mode;
        }
    }

    /**
     * 共享首选项封装类，采用代理模式，实现了{@link android.content.SharedPreferences}和
     * {@link android.content.SharedPreferences.Editor}的大部分接口
     */
    public static class Sp {
        SharedPreferences sp;

        /**
         * 构造方法
         *
         * @param context 上下文
         * @param name    共享首选项文件名
         * @param mode    操作模式
         * @throws NullPointerException 上下文为null或者 getSharedPreferences 返回null时抛出
         */
        Sp(Context context, String name, int mode) throws NullPointerException {
            sp = context.getSharedPreferences(name, mode);
            if (sp == null) {
                throw new NullPointerException("create sp failed: context is not ready!");
            }
        }

        public Map<String, ?> getAll() {
            return sp.getAll();
        }

        @Nullable
        public String getString(String key, @Nullable String defValue) {
            return sp.getString(key, defValue);
        }

        public JSONObject getJsonObj(String key) {
            JSONObject result = new JSONObject();
            String strJson = sp.getString(key, null);
            if (!TextUtils.isEmpty(strJson)) {
                try {
                    result = new JSONObject(strJson);
                } catch (JSONException e) {
                    Log.e("SPUtils", e.toString());
                }
            }
            return result;
        }

        @Nullable
        public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
            return sp.getStringSet(key, defValues);
        }

        public int getInt(String key, int defValue) {
            return sp.getInt(key, defValue);
        }

        public long getLong(String key, long defValue) {
            return sp.getLong(key, defValue);
        }

        public float getFloat(String key, float defValue) {
            return sp.getFloat(key, defValue);
        }

        public boolean getBoolean(String key, boolean defValue) {
            return sp.getBoolean(key, defValue);
        }

        public boolean contains(String key) {
            return sp.contains(key);
        }

        public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
            sp.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }

        public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
            sp.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }

        public void putString(String key, @Nullable String value) {
            sp.edit().putString(key, value).apply();
        }

        public void putStringSet(String key, @Nullable Set<String> values) {
            sp.edit().putStringSet(key, values).apply();
        }

        public void putInt(String key, int value) {
            sp.edit().putInt(key, value).apply();
        }

        public void putLong(String key, long value) {
            sp.edit().putLong(key, value).apply();
        }

        public void putFloat(String key, float value) {
            sp.edit().putFloat(key, value).apply();
        }

        public void putBoolean(String key, boolean value) {
            sp.edit().putBoolean(key, value).apply();
        }

        public void putBooleanImmediately(String key, boolean value) {
            sp.edit().putBoolean(key, value).commit();
        }

        public void remove(String key) {
            sp.edit().remove(key).apply();
        }

        public void clear() {
            sp.edit().clear().apply();
        }

        private SharedPreferences.Editor batchEditor() {
            return sp.edit();
        }
    }
}
