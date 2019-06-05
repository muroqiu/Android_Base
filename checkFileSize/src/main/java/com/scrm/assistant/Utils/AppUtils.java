package com.scrm.assistant.Utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 应用相关的工具类。
 *
 * @author Reinhard（李剑波）
 * @date 2018/6/26
 */
@SuppressWarnings("unused")
public class AppUtils {

    //软件类型判断软件
    //未知软件类型
    public static final int UNKNOW_APP = 0;
    //用户软件类型
    public static final int USER_APP = 1;
    //系统软件
    public static final int SYSTEM_APP = 2;
    //系统升级软件
    public static final int SYSTEM_UPDATE_APP = 3;
    private final static String TAG = "AppUtils";

    /**
     * 通过反射调用获取当前 Application 对象
     *
     * @return 当前 Application 对象
     */
    public static Application getApp() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object at = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(at);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    public static String getAppVersionName(Context context, String pkg) {
        String versionName = "unknow";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkg, 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getAppVersionCode(Context context, String pkg) {
        int versionCode = -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkg, 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static boolean isDebuggable() {
        return isDebuggable(getApp().getPackageName());
    }

    public static boolean isDebuggable(String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return false;
        }

        try {
            PackageManager pm = getApp().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(pkgName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取版本名称
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return 0;
    }

    /**
     * 获取版本名称
     *
     * @param mContext
     * @return
     */
    public static String getVersionName(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return "";
    }


    /**
     * 获取设备序列号
     *
     * @return
     */
    public static String getSerial() {
        return Build.SERIAL;
    }

    /**
     * 获取厂商名
     *
     * @return
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取产品名
     *
     * @return
     */
    public static String getDeviceProduct() {
        return Build.PRODUCT;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机主板名
     *
     * @return
     */
    public static String getDeviceBoard() {
        return Build.BOARD;
    }

    /**
     * 获取设备名
     *
     * @return
     */
    public static String getDeviceName() {
        return Build.DEVICE;
    }

    /**
     * 获取手机Android版本
     *
     * @return
     */
    public static String getDeviceAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getPackName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取应用名称
     */
    public static String getAppName(Context context, String packageName) {
        String appName = "";
        try {
            PackageManager packManager = context.getPackageManager();
            ApplicationInfo appInfo = context.getPackageManager().getPackageInfo(packageName, 0).applicationInfo;
            appName = (String) packManager.getApplicationLabel(appInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appName;
    }

    /**
     * 获取应用 meta-data 的值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getMetaDataValue(Context context, String key) {
        if (context == null) {
            return "";
        }
        String value = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                        context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null && applicationInfo.metaData != null) {
                    value = String.valueOf(applicationInfo.metaData.get(key));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 检查app是否是系统rom集成的
     *
     * @param packName
     * @return
     */
    public static int checkAppType(Context context, String packName) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(packName, 0);
            // 是系统软件或者是系统软件更新
            if (isSystemApp(pInfo)) {
                return SYSTEM_APP;
            } else if (isSystemUpdateApp(pInfo)) {
                return SYSTEM_UPDATE_APP;
            } else if (isUserApp(pInfo)) {
                return USER_APP;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return UNKNOW_APP;
    }

    /**
     * 是否是系统软件或者是系统软件的更新软件
     *
     * @return
     */
    public static boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    public static boolean isUserApp(PackageInfo pInfo) {
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
    }

    /**
     * isAppInstalled 判断APP是否安装
     *
     * @param context 上下文 pathName APP包名
     * @return
     */
    public static boolean isAppInstalled(Context context, String pathName) {
        ApplicationInfo ai;
        try {
            ai = context.getPackageManager().getApplicationInfo(pathName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Requested package doesn't exist: " + pathName);
            return false;
        }
    }

    /**
     * getSysPro 获取系统属性
     *
     * @param key 属性名称
     * @return
     */
    public static String getSysPro(String key) {
        String result = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            result = (String) get.invoke(c, key);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

}
