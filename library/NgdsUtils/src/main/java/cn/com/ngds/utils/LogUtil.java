package cn.com.ngds.utils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * 日志输出类
 * Created by ZhangWF(zhangwf0929@gmail.com) on 2017/7/4.
 */

public class LogUtil {

    private static final String TAG_CRASH = "crash";

    public static void init(String tag) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(tag)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        // 本地记录
        FormatStrategy fileStrategy = CsvFormatStrategy.newBuilder()
                .tag(TAG_CRASH)
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(fileStrategy));
    }

    public static void d(String tag, String message, Object... args) {
        Logger.t(tag).d(message, args);
    }

    public static void d(String tag, Object object) {
        Logger.t(tag).d(object);
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void d(Object object) {
        Logger.d(object);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void e(String message, Object... args) {
        Logger.e(null, message, args);
    }

    public static void e(Throwable throwable) {
        Logger.e(throwable, null);
    }

    public static void crash(Throwable throwable) {
        Logger.t(TAG_CRASH).e(throwable, null);
    }
}
