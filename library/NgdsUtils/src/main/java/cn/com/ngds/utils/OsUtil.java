package cn.com.ngds.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>手机系统<p/>
 * Created by Yubch(yubachang@newgame.com) on 16/6/3.
 */
public class OsUtil {
    private static final String KEY_MIUI = "is_miui";
    private static final int UN_KNOW = 0; //未知
    private static final int IS_MIUI = 1; //是miui系统
    private static final int NO_IS_MIUI = 2; //不是miui系统
    // 检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    private static final String KEY_VIVO = "vivo"; //vivo的硬件厂商
    private static final String KEY_OPPO = "OPPO"; //OPPO的硬件厂商

    /**
     * 判断是否为小米系统
     *
     * @param ctx
     * @return
     */
    public static boolean isMIUI(Context ctx) {
        int key = ConfigHelper.getInstance(ctx).loadIntKey(KEY_MIUI, UN_KNOW);
        if (IS_MIUI == key) {
            return true;
        } else if (NO_IS_MIUI == key) {
            return false;
        }
        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        ConfigHelper.getInstance(ctx).saveIntKey(KEY_MIUI, isMIUI ? IS_MIUI : NO_IS_MIUI);
        return isMIUI;
    }

    /**
     * 判断是否为vivo系统
     *
     * @return
     */
    public static boolean isVivo() {
        return KEY_VIVO.equals(android.os.Build.MANUFACTURER);
    }

    /**
     * 判断是否为OPPO系统
     *
     * @return
     */
    public static boolean isOppo() {
        return KEY_OPPO.equals(android.os.Build.MANUFACTURER);
    }
}
