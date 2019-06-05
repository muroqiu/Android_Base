package com.scrm.assistant.Utils;

import android.util.Log;

/**
 * Created by TRZ on 2017/4/10.
 * shell 脚本的执行工具类
 */
public class Simulator {
    private static final String TAG = "Simulator";

    /**
     * 执行uiautomator Jar 自动化测试
     * <p>
     * resultCode 0 sucess   1 failed
     *
     * @param cmd
     * @return
     */
    public static String execute(String cmd) {
        Log.d(TAG, "-----cmd---" + cmd);
        RootUtil rootUtil = RootUtil.getInstance();
        if (!rootUtil.startShell()) {
            Log.d(TAG, " root failed ");
            return "Failed to get root access.";
        }
        RootUtil.LineCallback callback = new RootUtil.CollectingLineCallback();
        int resultCode = rootUtil.execute(cmd, callback);
        Log.d(TAG, "resultCode = " + resultCode);
        StringBuilder resultMessage = new StringBuilder(callback.toString());
        // Log.d(TAG, "resultMessage = "+resultMessage.toString());
        if (resultCode == 0) {
            return resultMessage.toString();
        } else {
            String errorStr = resultMessage.toString();
            if (errorStr.contains("Permission") || errorStr.contains("EPIPE")) {
                throw new RunException("未授权ROOT" + "权限");
            } else if (errorStr.contains("Utf7ImeService")) {
                throw new RunException("未正常安装51zan输入法，请在输入法设置查看是否安装");
            } else if (errorStr.contains("INSTALL_FAILED_INVALID_APK")) {
                throw new RunException("未正常安装51zan输入法，请在输入法设置查看是否安装");
            }
        }
        return "";
    }

    /**
     * 销毁进程
     *
     * @param process 进程
     */
    private static void processDestroy(Process process) {
        if (process != null) {
            try {
                //判断是否正常退出
                if (process.exitValue() != 0) {
                    killProcess(process);
                }
            } catch (IllegalThreadStateException e) {
                killProcess(process);
            }
        }
    }

    /**
     * 通过Android底层实现进程关闭
     *
     * @param process 进程
     */
    private static void killProcess(Process process) {
        int pid = getProcessId(process);
        if (pid != 0) {
            try {
                android.os.Process.killProcess(pid);
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取进程的ID
     *
     * @param process 进程
     * @return
     */
    private static int getProcessId(Process process) {
        String str = process.toString();
        try {
            int i = str.indexOf("=") + 1;
            int j = str.indexOf("]");
            str = str.substring(i, j);
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

}
