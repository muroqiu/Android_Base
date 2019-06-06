package com.scrm.im.Utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author 曾广贤  muroqiu@qq.com
 * @describe 下载文件缓存
 * @date 2018/10/26
 */
public class UriCacher {
    private static final String FIELDNAME_URI = "uri";
    private static final String FIELDNAME_MD5 = "md5";
    private static final String FIELDNAME_TIMES = "times";

    private String uri = "";
    private String md5 = "";
    // 时间戳 秒
    private int times = 0;

    public UriCacher(String uri, String md5, int times) {
        this.uri = uri;
        this.md5 = md5;
        this.times = times;
    }

    public UriCacher(String uri, String md5) {
        this(uri, md5, TimeUtils.seconds());
    }

    public UriCacher(JSONObject dataJsonObject) throws JSONException {
        uri = dataJsonObject.optString(FIELDNAME_URI);
        md5 = dataJsonObject.optString(FIELDNAME_MD5);
        times = dataJsonObject.optInt(FIELDNAME_TIMES);
    }

    public UriCacher(String dataStr) throws JSONException {
        this(new JSONObject(dataStr));
    }

    public String getUri() {
        return uri;
    }

    public String getMd5() {
        return md5;
    }

    public int getTimes() {
        return times;
    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(FIELDNAME_URI, uri);
            jsonObject.put(FIELDNAME_MD5, md5);
            jsonObject.put(FIELDNAME_TIMES, times);
        } catch (JSONException e) {
            Log.e("DownLoadCacher", e.toString());
        }

        return jsonObject.toString();
    }
}
