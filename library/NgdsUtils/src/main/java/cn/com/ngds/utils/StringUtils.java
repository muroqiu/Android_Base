package cn.com.ngds.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangyt on 2017/11/23.
 * : 字符串工具类
 */

public class StringUtils {

    public static String toThumbImageUrl128(String url) {
        return url + "@w128";
    }

    public static String toThumbImageUrl240(String url) {
        return url + "@w240";
    }

    public static String toThumbImageUrl320(String url) {
        return url + "@w320";
    }

    public static String toThumbImageUrl480(String url) {
        return url + "@w480";
    }

    public static String toThumbImageUrl640(String url) {
        return url + "@w640";
    }

    public static String toThumbImageUrl720(String url) {
        return url + "@w720";
    }

    public static String toThumbImageUrl960(String url) {
        return url + "@w960";
    }

    public static String toThumbImageUrl1024(String url) {
        return url + "@w1024";
    }

    /**
     * 数量转化成 字符串 如果
     *
     * @param num
     * @param context
     * @return
     */
    public static String numToText(int num, Context context) {
        if (num > 10000) {
            float newNum = num / 10000f;
            DecimalFormat df = new DecimalFormat("###.00");
            return context.getString(R.string.num_text, df.format(newNum));
        }
        return String.valueOf(num);
    }

    /**
     * 返回 万为单位、不带小数点数字 的字符串
     *
     * @param num
     * @return
     */
    public static String formatTenThousandUnit(int num) {
        String strUnit = "万";
        int unit = 10000;
        if (num >= unit) {
            return num / unit + strUnit;
        } else {
            return String.valueOf(num);
        }
    }

    public static String formatCount(Context context, int count) {
        if (context == null || count < 0) {
            return "0";
        }
        if (count < 10000) {
            // 小于1万
            return String.valueOf(count);
        }
        int unit = R.string.ten_thousand;
        if (count >= 100000000) {
            // 大于等于1亿
            count = count / 10000;
            unit = R.string.a_hundred_million;
        }
        // 避免出现.0
        String format = count % 10000 < 1000 ? "%.0f%s" : "%.1f%s";
        return String.format(format, count / 10000f, context.getString(unit));
    }

    /**
     * 获取文件大小，单位G、M、K
     *
     * @param length 字节数
     * @return
     */
    public static String convertFileSize(long length) {
        String result;
        if (length < 1024) {
            length = length > 0 ? length : 0; // 保证大于等于0
            result = length + "B";
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            if (length < 1024 * 1024) {
                result = df.format((double) length / 1024) + "K";
            } else if (length < 1024 * 1024 * 1024) {
                result = df.format((double) length / 1024 / 1024) + "M";
            } else {
                result = df.format((double) length / 1024 / 1024 / 1024) + "G";
            }
        }
        return result;
    }

    /**
     * 手柄版本转换
     *
     * @param version
     * @return
     */
    public static String convertDeviceVersion(String version) {
        String result = version;
        if (version != null && version.length() > 1) {
            result = "V" + version.substring(0, 1) + "." + version.substring(1);
        }
        return result;
    }

    /**
     * 将长字符串转换为省略号结尾的短字符串
     *
     * @param s
     * @param maxLength
     * @return
     */
    public static String getTextByEllipsize(String s, int maxLength) {
        if (!TextUtils.isEmpty(s) && maxLength > 0 && s.length() > maxLength) {
            s = s.substring(0, maxLength) + "...";
        }
        return s;
    }

    /**
     * 关键字高亮变色 忽略大小写
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字
     * @return 结果SpannableString
     */
    public static SpannableString matcherKeyWord(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text.toLowerCase());
        SpannableString result = new SpannableString(text);
        keyword = escapeKeyWord(keyword.toLowerCase());
        text = escapeKeyWord(text.toLowerCase());
        if (text.contains(keyword) && !TextUtils.isEmpty(keyword)) {
            try {
                Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                Matcher m = p.matcher(s);
                while (m.find()) {
                    int start = m.start();
                    int end = m.end();
                    result.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return keyword
     */
    public static String escapeKeyWord(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
