package com.scrm.test.sqlite;

import android.text.TextUtils;
import android.util.Log;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;


/**
 * Created by zsf on 2018/7/19.
 * 日志工具
 * <p>
 * 日志格式：日期 时间 [线程] 等级 进程名 - 标签: 消息[异常堆栈]
 * <p>
 * 2018-08-17 17:18:49.525 [parallels-4] ERROR log_file - DBHelper: reserved3
 * <p>
 * 使用方法：
 * <p>
 * 1、在自定义{@link android.app.Application}的入口（如 onCreate() ）调用{@link #init(String)}，完成初始化
 * <p>
 * 2、调用对应的日志打印接口（支持系统自带{@link Log}对应的全部打印接口）
 */
public class LogUtils {

    private static final String TAG = "LogUtils";
    /**
     * Logger的调试等级
     */
    private static final Level LOGGER_LEVEL = Level.DEBUG;
    private static final int DEFAULT_TRACE_LEVEL = 4;
    /**
     * log文件保留的最长时间
     */
//    private static final int MAX_HISTORY = 200;
//
//    private static final String MAX_FILE_SIZE = "10mb";
//    private static final String MAX_TOTAL_SIZE = "500mb";

    // for test
    private static final int MAX_HISTORY = 2;

    private static final String MAX_FILE_SIZE = "2kb";
    private static final String MAX_TOTAL_SIZE = "10kb";
    /**
     * 日志输出中的进程名，可以通过主动调用{@link #init(String)}进行设置
     */
    public static String sProcessName = "com.scrm.im";
    /**
     * 是否存储log信息，后续酌情选择开启或者关闭
     */
    private static boolean isDebugLogOpen = true;
    private static ch.qos.logback.classic.Logger sLogger;

    private LogUtils() {
    }

    /**
     * 初始化
     *
     * @param processName 日志输出中的进程名
     */
    public static void init(String processName) {
//        String fileNamePattern = getLogFileDir() + "debug_%d{yyyyMMddHH}_%i.log.zip";
        // for test
        String fileNamePattern = getLogFileDir() + "debug_%d{yyyyMMddHHmm}_%i.log.zip";
        Log.d(TAG, "init: processName = " + processName + ", fileNamePattern = " + fileNamePattern);
        RollingFileAppender rollingFileAppender = configureLogbackAppender(fileNamePattern);
        sLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(processName);
        sLogger.setLevel(LOGGER_LEVEL);
        sLogger.addAppender(rollingFileAppender);
        sProcessName = processName;
    }

    /**
     * @return 日志打印的通用Tag
     */
    private static String generateTag(int level) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[level];
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return String.format(Locale.CHINA, "%s.%s(L:%d)", callerClazzName, caller.getMethodName(),
                caller.getLineNumber());
    }

    private static RollingFileAppender configureLogbackAppender(String fileNamePattern) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(context);
        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
        rollingPolicy.setFileNamePattern(fileNamePattern);
        rollingPolicy.setMaxHistory(MAX_HISTORY);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setContext(context);
        rollingPolicy.setTotalSizeCap(FileSize.valueOf(MAX_TOTAL_SIZE));
        SizeAndTimeBasedFNATP<ILoggingEvent> sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP<>();
        sizeAndTimeBasedFNATP.setMaxFileSize(FileSize.valueOf(MAX_FILE_SIZE));
        sizeAndTimeBasedFNATP.setContext(context);
        sizeAndTimeBasedFNATP.setTimeBasedRollingPolicy(rollingPolicy);
        rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);
        rollingPolicy.start();
        sizeAndTimeBasedFNATP.start();
        rollingFileAppender.setRollingPolicy(rollingPolicy);
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.setContext(context);
        encoder.start();
        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();
        return rollingFileAppender;
    }

    private static RollingFileAppender configureLogbackAppender2(String fileNamePattern) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(context);

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setFileNamePattern(fileNamePattern);
        rollingPolicy.setMaxFileSize(FileSize.valueOf(MAX_FILE_SIZE));
        rollingPolicy.setMaxHistory(MAX_HISTORY);
        rollingPolicy.setTotalSizeCap(FileSize.valueOf(MAX_TOTAL_SIZE));
        rollingPolicy.setContext(context);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.start();
        rollingFileAppender.setRollingPolicy(rollingPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.setContext(context);
        encoder.start();
        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();

        return rollingFileAppender;
    }

    /**
     * 确保日志文件存在，因为在运行时删除文件，logback不会自动创建
     */

    private static void ensureLogFileExist() {
        String logFileDir = getLogFileDir();
        if ((!new File(logFileDir).exists()) || (sLogger == null)) {
            Log.d(TAG, "ensureLogFileExist: logFileDir = " + logFileDir
                    + " sLogger = " + sLogger);
            init(sProcessName);
        }
    }

    /**
     * 获取日志文件存放的文件夹路径
     */
    public static String getLogFileDir() {
        return "/sdcard/scrm/logbacktest/";
    }

    public static void setIsDebugLogOpen(boolean isDebugLogOpen) {
        LogUtils.isDebugLogOpen = isDebugLogOpen;
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
        log(Log.VERBOSE, tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        Log.v(tag, msg, tr);
        log(Log.VERBOSE, tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
        log(Log.DEBUG, tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        Log.d(tag, msg, tr);
        log(Log.DEBUG, tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
        log(Log.INFO, tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        Log.i(tag, msg, tr);
        log(Log.INFO, tag, msg, tr);
    }

    /**
     * 自己添加，{@link Log}无此接口
     */
    public static void w(String msg) {
        String tag = generateTag(DEFAULT_TRACE_LEVEL);
        w(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
        log(Log.WARN, tag, msg);
    }

    public static void w(String tag, Throwable tr) {
        Log.w(tag, tr);
        log(Log.WARN, tag, tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        Log.w(tag, msg, tr);
        log(Log.WARN, tag, msg, tr);
    }

    /**
     * 自己添加，{@link Log}无此接口
     */
    public static void e(String msg) {
        String tag = generateTag(DEFAULT_TRACE_LEVEL);
        e(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
        log(Log.ERROR, tag, msg);
    }

    /**
     * 自己添加，{@link Log}无此接口
     */
    public static void e(String tag, Throwable tr) {
        e(tag, "", tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
        log(Log.ERROR, tag, msg, tr);
    }

    public static void wtf(String tag, String msg) {
        Log.wtf(tag, msg);
        log(Log.ASSERT, tag, msg);
    }

    public static void wtf(String tag, Throwable tr) {
        Log.wtf(tag, tr);
        log(Log.ASSERT, tag, tr);
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        Log.wtf(tag, msg, tr);
        log(Log.ASSERT, tag, msg, tr);
    }

    private static void log(int level, String tag, String msg) {
        log(level, tag, msg, null);
    }

    private static void log(int level, String tag, Throwable tr) {
        log(level, tag, null, tr);
    }

    private static void log(int level, String tag, String msg, Throwable tr) {
        if (!isDebugLogOpen) {
            return;
        }

        ensureLogFileExist();

        if (TextUtils.isEmpty(tag)) {
            tag = "";
        }
        StringBuilder logMsg = new StringBuilder(tag).append(": ");
        if (!TextUtils.isEmpty(msg)) {
            logMsg.append(msg);
        }
        if (tr != null) {
            logMsg.append("\n").append(Log.getStackTraceString(tr));
        }
        switch (level) {
            case Log.VERBOSE:
                sLogger.trace(logMsg.toString());
                break;
            case Log.DEBUG:
                sLogger.debug(logMsg.toString());
                break;
            case Log.INFO:
                sLogger.info(logMsg.toString());
                break;
            case Log.WARN:
                sLogger.warn(logMsg.toString());
                break;
            case Log.ERROR:
                sLogger.error(logMsg.toString());
                break;
            case Log.ASSERT:
                sLogger.error(logMsg.toString());
                break;
            default:
                break;
        }
    }

}


