package cn.com.ngds.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by ChenYuYun
 * 创建时间 17/2/9
 * 剪切版工具类
 */
public class ClipboardUtils {
    public static final String KEY_TEXT = "GamePad";

    /**
     * 保存
     *
     * @param text
     * @param context
     */
    public static void save(Context context, CharSequence text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(KEY_TEXT, text));

    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip()) {
            return clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
        }
        return "";
    }
}
