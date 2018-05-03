package cn.com.ngds.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期时间工具类
 */
public class DateUtils {

    private static final long ONE_MINUTE = 1000 * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long TWO_DAY = ONE_DAY * 2;

    private DateUtils() {
    }

    public static String getStrTime(long timeInSec) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        re_StrTime = sdf.format(new Date(timeInSec * 1000L));
        return re_StrTime;
    }

    public static String getStrTimeM(long timeInSec) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        re_StrTime = sdf.format(new Date(timeInSec * 1000L));
        return re_StrTime;
    }

    public static String getStrDate(long timeInSec) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        re_StrTime = sdf.format(new Date(timeInSec * 1000L));
        return re_StrTime;
    }

    public static String getStrDateCn(long timeInSec) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        re_StrTime = sdf.format(new Date(timeInSec * 1000L));
        return re_StrTime;
    }

    public static String getStrDateSlot(long timeInSec) {
        Date date = new Date(timeInSec);
        int month = date.getMonth() + 1;
        int day = date.getDate();
        return month + "月" + day + "日 燃情上线";
    }

    /**
     * 计算两个日期相隔的天数，如果endDay在beginDay之后，返回正数，否则返回负数
     *
     * @param beginDayInMillis
     * @param endDayInMillis
     * @return
     */
    public static int getDaysBetween(long beginDayInMillis, long endDayInMillis) {
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        begin.setTimeInMillis(Math.min(beginDayInMillis, endDayInMillis));
        end.setTimeInMillis(Math.max(beginDayInMillis, endDayInMillis));
        // 计算当年时间差
        int betweenDays = end.get(Calendar.DAY_OF_YEAR) - begin.get(Calendar.DAY_OF_YEAR);
        int betweenYears = end.get(Calendar.YEAR) - begin.get(Calendar.YEAR);
        // 累计年份天数
        for (int i = 0; i < betweenYears; i++) {
            begin.set(Calendar.YEAR, (begin.get(Calendar.YEAR) + 1));
            betweenDays += begin.getMaximum(Calendar.DAY_OF_YEAR);
        }
        return endDayInMillis > beginDayInMillis ? betweenDays : betweenDays * -1;
    }

    /**
     * 计算某个日期与今天相隔的天数，如果今天之前，返回负数，否则返回正数
     *
     * @param dayInSec
     * @return
     */
    public static int getDaysBetweenToday(long dayInSec) {
        return getDaysBetween(System.currentTimeMillis(), dayInSec * 1000);
    }

    /**
     * 返回格式化日期 [月－日]
     *
     * @param timeInSec
     * @return
     */
    public static String formatMonthDay(long timeInSec) {
        Date d = new Date(timeInSec * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("[MM-dd]");
        return sf.format(d);
    }

    /**
     * 时间戳转日期
     *
     * @param pattern   yyyy-MM-dd HH:mm:ss
     * @param timeInSec
     * @return
     */
    public static String formatTime(String pattern, long timeInSec) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(new Date(timeInSec * 1000L));
    }


    /**
     * 按格式，返回格式化日期
     *
     * @param format
     * @param timeInSec
     * @return
     */
    public static String formatDate(String format, long timeInSec) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeInSec * 1000);
        // 2014年5月5日
        return String.format(format, time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1,
                time.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 返回格式化时间，“刚刚、x分钟前、今天14:23...”
     *
     * @param context
     * @param timeInMillis
     * @return
     */
    @SuppressLint("StringFormatMatches")
    public static String formatTime(Context context, long timeInMillis) {
        String result;
        long offset = System.currentTimeMillis() - timeInMillis;
        if (offset < ONE_MINUTE) {
            // <60秒——刚刚
            result = context.getString(R.string.time_format_just);
            return result;
        } else if (offset < ONE_HOUR) {
            // 1分钟前——59分钟前
            result = String.format(context.getString(R.string.time_format_minutes_before),
                    (int) offset / ONE_MINUTE);
            return result;
        } else {
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(timeInMillis);
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());

            if (isSameDay(time, now)) {
                // 今天 13:05
                result = String.format(context.getString(R.string.time_format_today),
                        time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
                return result;
            } else if (isSameYear(time, now)) {
                // 5月5日 14:27
                result = String.format(context.getString(R.string.time_format_this_year),
                        time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH),
                        time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
                return result;
            } else {
                // 2014年5月5日 14:27
                result = String.format(context.getString(R.string.time_format_years_before),
                        time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1,
                        time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE));
                return result;
            }
        }
    }

    /**
     * 返回格式化时间，“刚刚、x分钟前、今天14:23，昨天 13:00，5-5 14:27，2016-12-12 14:78”
     *
     * @param context
     * @param timeInMillis
     * @return
     */
    public static String formatTimeMore(Context context, long timeInMillis) {
        String result;
        long offset = System.currentTimeMillis() - timeInMillis;
        if (offset < ONE_MINUTE) {
            // <60秒——刚刚
            result = context.getString(R.string.time_format_just);
            return result;
        } else if (offset < ONE_HOUR) {
            // 1分钟前——59分钟前
            result = String.format(context.getString(R.string.time_format_minutes_before),
                    (int) offset / ONE_MINUTE);
            return result;
        } else {
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(timeInMillis);
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());

            if (isSameDay(time, now)) {
                // 今天 13:05
                result = String.format(context.getString(R.string.time_format_today),
                        time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
                return result;
            } else if (isYesterdayDay(time, now)) {
                // 昨天 13:05
                result = String.format(context.getString(R.string.time_format_yesterday_string),
                        time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
                return result;
            } else if (isSameYear(time, now)) {
                // 5-5 14:27
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
                simpleDateFormat.format(new Date(timeInMillis));
                return simpleDateFormat.format(new Date(timeInMillis));
            } else {
                // 2014-5-5日 14:27
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                simpleDateFormat.format(new Date(timeInMillis));
                return simpleDateFormat.format(new Date(timeInMillis));
            }
        }
    }

    public static boolean isSameDay(Calendar time1, Calendar time2) {
        return isSameYear(time1, time2) && isSameMonth(time1, time2) &&
                time1.get(Calendar.DAY_OF_MONTH) == time2.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isYesterdayDay(Calendar time1, Calendar time2) {
        return isSameYear(time1, time2) && isSameMonth(time1, time2) &&
                time1.get(Calendar.DAY_OF_MONTH) == time2.get(Calendar.DAY_OF_MONTH) - 1;
    }

    public static boolean isSameMonth(Calendar time1, Calendar time2) {
        return isSameYear(time1, time2) &&
                time1.get(Calendar.MONTH) == time2.get(Calendar.MONTH);
    }

    public static boolean isSameYear(Calendar time1, Calendar time2) {
        return time1.get(Calendar.YEAR) == time2.get(Calendar.YEAR);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNow(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String getDate(String format, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 获取当前日前
     *
     * @return
     */
    public static String getNowDate() {
        return getNow("yyyy-MM-dd");
    }

    //时间计数器，最多只能到99小时，如需要更大小时数需要改改方法
    public static String showTimeCount(long time) {
        if (time >= 360000) {
            return "00:00:00";
        }
        String timeCount = "";
        long hourc = time / 3600;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        long minuec = (time - hourc * 3600) / (60);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        long secc = time - hourc * 3600 - minuec * 60;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = hour + ":" + minue + ":" + sec;
        return timeCount;
    }

    /**
     * @param timeInSec
     * @return
     */
    public static boolean isSameDay(long timeInSec) {
        return isSameDayByJava(timeInSec * 1000);
    }

    /**
     * @param timeInSec
     * @return
     */
    public static boolean isSameDayByJava(long timeInSec) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeInSec);
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        return isSameDay(time, now);
    }
}
