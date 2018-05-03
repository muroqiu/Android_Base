package cn.com.ngds.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ApplicationUtils {

    /**
     * 返回当前程序的版本编号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo packInfo = null;
        try {
            packInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
        return packInfo.versionCode;
    }

    /**
     * 返回当前程序的版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo packInfo = null;
        try {
            packInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return packInfo.versionName;
    }

    /**
     * 返回安卓系统版本名称
     *
     * @return
     */
    public static String getOsVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 返回安卓机型
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 功能：获取内核
     *
     * @return
     * @author: by Fooyou 2014年2月6日 下午2:51:00
     */
    public static String getKernelVersion() {
        String kernelVersion = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/proc/version");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return kernelVersion;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8 * 1024);
        String info = "";
        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                info += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (!info.equals("")) {
                final String keyword = "version ";
                int index = info.indexOf(keyword);
                line = info.substring(index + keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

    /**
     * 获取本地已安装的第三方app
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getInstalledApps(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<PackageInfo> result = new ArrayList<PackageInfo>();
        for (PackageInfo app : packages) {
            //过滤非系统软件
            if ((app.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                result.add(app);
            }
        }
        return result;
    }

    /**
     * 根据包名，获取本地已安装程序的信息
     *
     * @param context
     * @param packageName
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo app : packages) {
            //过滤非系统软件
            if (app.packageName.equals(packageName)) {
                return app;
            }
        }
        return null;
    }

    /**
     * 根据资源id生成Uri
     *
     * @param context
     * @param resId
     * @return
     */
    public static Uri getUriByRes(Context context, int resId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(resId) + "/"
                + context.getResources().getResourceTypeName(resId) + "/"
                + context.getResources().getResourceEntryName(resId));
    }

    /**
     * 根据安装、卸载等广播中的包名；包名的格式为 "package:xxx.xxx.xxx"
     *
     * @param packageIntent
     * @return
     */
    public static String getPackageByIntent(Intent packageIntent) {
        if (packageIntent != null) {
            String data = packageIntent.getDataString();
            if (!TextUtils.isEmpty(data)) {
                int idx = data.indexOf(":") + 1;
                return data.substring(idx);
            }
        }
        return null;
    }

    /**
     * 用户安装的apk
     *
     * @param applicationInfo
     * @return
     */
    public static boolean isUserInstalledApp(ApplicationInfo applicationInfo) {
        if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
            // 代表的用户的应用
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断程序是否在前台
     * 进程很多的情况将是一个耗时的循环
     *
     * @param context
     * @return
     */
    public static boolean isInForeground(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    //前台程序
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                return true;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否是模拟器
     */
    public static String isEmulator(Context context) {
        return "" + isEmulator2() + isEmulator3(context);
    }

    private static int isEmulator2() {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)) ? 1 : 0;
    }

    private static int isEmulator3(Context context) {
        String szOperatorName = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
        if (szOperatorName.toLowerCase().equals("android")) {
            return 1;
        }
        return 0;
    }


    /**
     * 判断是否安装了某个APP
     *
     * @param context
     * @param packagename
     * @return
     */
    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return packageInfo != null;
    }

    /**
     * 运行apk
     *
     * @param context
     * @param packageName
     * @return
     */
    public static void runApk(Context context, String packageName) throws
            PackageManager.NameNotFoundException {
        PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps =
                context.getPackageManager().queryIntentActivities(resolveIntent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 要开一个新task，避免覆盖时出错
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ComponentName cn = new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    private static final String APK_TYPE = "application/vnd.android.package-archive";

    /**
     * 安装apk
     *
     * @param context
     * @param apkFile
     * @return
     */
    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), APK_TYPE);
        // 要开一个新task，避免覆盖时出错
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载apk
     *
     * @param context
     * @param packageName
     * @return
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent =
                new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
        // 要开一个新task，避免完成时关闭当前的界面
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开系统浏览器
     *
     * @param ctx
     * @param url
     */
    public static void openSystemBrowser(Context ctx, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        ctx.startActivity(intent);
    }
}
