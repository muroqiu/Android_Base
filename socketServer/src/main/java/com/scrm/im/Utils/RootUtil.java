package com.scrm.im.Utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;
import eu.chainfire.libsuperuser.Shell.OnCommandResultListener;

public class RootUtil {

    private static RootUtil instance = null;
    private RootUtil(){}
    public  static synchronized  RootUtil getInstance()
    {
        if( instance == null)
            instance = new RootUtil();
        return instance;
    }

    private static final String TAG = "RootUtil";
    private Shell.Interactive mShell = null;
    private HandlerThread mCallbackThread = null;

    private boolean mCommandRunning = false;
    private int mLastExitCode = -1;
    public LineCallback mCallback = null;

    private static final String EMULATED_STORAGE_SOURCE;
    private static final String EMULATED_STORAGE_TARGET;

    static {
        EMULATED_STORAGE_SOURCE = getEmulatedStorageVariable("EMULATED_STORAGE_SOURCE");
        EMULATED_STORAGE_TARGET = getEmulatedStorageVariable("EMULATED_STORAGE_TARGET");
    }

    public interface LineCallback {
        void onLine(String line);
        void onErrorLine(String line);
    }

    public static class CollectingLineCallback implements LineCallback {
        protected List<String> mLines = new LinkedList<>();

        @Override
        public void onLine(String line) {
            mLines.add(line);
        }

        @Override
        public void onErrorLine(String line) {
            mLines.add(line);
        }

        @Override
        public String toString() {
            return TextUtils.join("\n", mLines);
        }
    }

    public static class LogLineCallback implements LineCallback {
        @Override
        public void onLine(String line) {
            Log.i(TAG, line);
        }

        @Override
        public void onErrorLine(String line) {
            Log.e(TAG, line);
        }
    }

    private static String getEmulatedStorageVariable(String variable) {
        String result = System.getenv(variable);
        if (result != null) {
            result = getCanonicalPath(new File(result));
            if (!result.endsWith("/")) {
                result += "/";
            }
        }
        return result;
    }


    private final Shell.OnCommandResultListener mOpenListener = new Shell.OnCommandResultListener() {
        @Override
        public void onCommandResult(int commandCode, int exitCode, List<String> output) {
            mStdoutListener.onCommandResult(commandCode, exitCode);
        }
    };

    private final Shell.OnCommandLineListener mStdoutListener = new Shell.OnCommandLineListener() {
        public void onLine(String line) {
            if (mCallback != null) {
                mCallback.onLine(line);
            }
        }

        @Override
        public void onCommandResult(int commandCode, int exitCode) {
            mLastExitCode = exitCode;
            synchronized (mCallbackThread) {
                mCommandRunning = false;
                mCallbackThread.notifyAll();
            }
        }
    };

    private final Shell.OnCommandLineListener mStderrListener = new Shell.OnCommandLineListener() {
        @Override
        public void onLine(String line) {
            if (mCallback != null) {
                mCallback.onErrorLine(line);
            }
        }

        @Override
        public void onCommandResult(int commandCode, int exitCode) {
            // Not called for STDERR listener.
        }
    };

    private void waitForCommandFinished() {
        synchronized (mCallbackThread) {
            while (mCommandRunning) {
                try {
                    mCallbackThread.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (mLastExitCode == OnCommandResultListener.WATCHDOG_EXIT || mLastExitCode == OnCommandResultListener.SHELL_DIED) {
            dispose();
        }
    }

    /**
     * Starts an interactive shell with root permissions. Does nothing if
     * already running.
     *
     * @return true if root access is available, false otherwise
     */
    public synchronized boolean startShell() {
        if (mShell != null) {
            if (mShell.isRunning()) {
                return true;
            } else {
                dispose();
            }
        }
        mCallbackThread = new HandlerThread("su callback listener");
        mCallbackThread.start();

        mCommandRunning = true;

        String shellStr = getSuCommand();

        mShell = new Shell.Builder().setShell(shellStr)
                .setHandler(new Handler(mCallbackThread.getLooper()))
                .setOnSTDERRLineListener(mStderrListener)
                .open(mOpenListener);

        waitForCommandFinished();

        if (mLastExitCode != OnCommandResultListener.SHELL_RUNNING) {
            dispose();
            return false;
        }

        return true;
    }


    /**
     * Closes all resources related to the shell.
     */
    public synchronized void dispose() {
        if (mShell == null) {
            return;
        }

        try {
            mShell.close();
        } catch (Exception ignored) {
        }
        mShell = null;

        mCallbackThread.quit();
        mCallbackThread = null;
    }

    public synchronized int execute(String command, LineCallback callback) {
        if (mShell == null) {
            throw new IllegalStateException("shell is not running");
        }

        mCallback = callback;
        mCommandRunning = true;
        mShell.addCommand(command, 0, mStdoutListener);
        waitForCommandFinished();

        return mLastExitCode;
    }



    private static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            Log.w(TAG, "Could not get canonical path for " + file);
            return file.getAbsolutePath();
        }
    }

    public static String getShellPath(File file) {
        return getShellPath(getCanonicalPath(file));
    }

    public static String getShellPath(String path) {
        if (EMULATED_STORAGE_SOURCE != null && EMULATED_STORAGE_TARGET != null
                && path.startsWith(EMULATED_STORAGE_TARGET)) {
            path = EMULATED_STORAGE_SOURCE + path.substring(EMULATED_STORAGE_TARGET.length());
        }
        return path;
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
    }

    public boolean softReboot(LineCallback callback) {
        return execute("setprop ctl.restart surfaceflinger; setprop ctl.restart zygote", callback) == 0;
    }

    private static final String SU_PATH = "/system/xbin/scrm";
    private static final String OTHER_SCRM = "/sbin/scrm";

    /**
     * 判断当前手机是否srcm or su
     *
     * @return
     */
    public static String getSuCommand() {
        if ((new File(SU_PATH).exists()) || (new File(OTHER_SCRM).exists())) {
            return "scrm";
        } else {
            return "su";
        }
    }
}
