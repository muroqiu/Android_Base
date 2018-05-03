package cn.com.ngds.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * Created by ZhangWF(zhangwf0929@gmail.com) on 2017/11/9.
 */

public class ToastUtil {

    /**
     * 提示toast
     *
     * @param context 上下文环境
     * @param resId   提示内容的资源id
     */
    public static void longShow(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void longShow(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void shortShow(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void shortShow(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
