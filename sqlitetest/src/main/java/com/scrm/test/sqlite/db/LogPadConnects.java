package com.scrm.test.sqlite.db;

/**
 * 手柄连接日志对象
 *
 * @author 慕容秋 (muroqiu@qq.com)
 * Create on 15/2/9
 */
public class LogPadConnects extends BaseType {
    // 手柄硬件标识
    private String mSign = "";
    // 记录时间，毫秒
    private long mLogTime;

    private String mUUID = "";

    public String getSign() {
        return mSign;
    }

    public void setSign(String sign) {
        this.mSign = sign;
    }

    public long getLogTime() {
        return mLogTime;
    }

    public void setLogTime(long logTime) {
        this.mLogTime = logTime;
    }

    public String getUUID() {
        return mUUID;
    }

    public void setUUID(String mUUID) {
        this.mUUID = mUUID;
    }
}
