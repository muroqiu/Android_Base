package cn.com.ngds.utils;


import android.os.SystemClock;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 系统崩溃处理类
 * <p/>
 * Created by ZhangWF(zhangwf0929@gmail.com) on 17-11-8.
 */

public class CrashHelper implements UncaughtExceptionHandler {

    /**
     * 初始化
     */
    public static void init() {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHelper());
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //获取系统默认的UncaughtException处理器
        UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!handleException(ex) && defaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler.uncaughtException(thread, ex);
        } else {
            SystemClock.sleep(1000);
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 记录日志
        LogUtil.crash(ex);
        return true;
    }
}
