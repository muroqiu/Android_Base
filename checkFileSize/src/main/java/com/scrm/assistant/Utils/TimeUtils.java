package com.scrm.assistant.Utils;

/**
 * @describe 时间函数集合
 *
 * @author 曾广贤  muroqiu@qq.com
 * @date 2018/10/28
 */
public class TimeUtils {

    /**
     * 返回秒级时间戳
     *
     * @return Returns the number of seconds since January 1, 1970, 00:00:00 GMT
     */
    public static int seconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
