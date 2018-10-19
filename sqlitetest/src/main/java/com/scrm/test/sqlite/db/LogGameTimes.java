package com.scrm.test.sqlite.db;

/**
 * 应用运行日志对象
 *
 * @author 慕容秋 (muroqiu@qq.com)
 * Create on 15/2/9
 */
public class LogGameTimes extends BaseType {
    // 配对设备名称
    private String mDeviceName = "";
    // 游戏名称
    private String mAppName = "";
    // 包名
    private String mPackageName = "";
    // 开始时间,UNIX时间戳
    private long mBeginTime;
    // 结束时间,UNIX时间戳
    private long mEndTime;
    // 运行时间,单位分钟
    private long mRunningTime;
    // 手柄硬件标识
    private String mSign = "";
    // 记录时间，毫秒
    private long mLogTime;

    private String mUUID;

    public String getDevice_name() {
        return mDeviceName;
    }

    public void setDevice_name(String device_name) {
        this.mDeviceName = device_name;
    }

    public String getApp_name() {
        return mAppName;
    }

    public void setApp_name(String app_name) {
        this.mAppName = app_name;
    }

    public String getPackage_name() {
        return mPackageName;
    }

    public void setPackage_name(String package_name) {
        this.mPackageName = package_name;
    }

    public long getRunning_time() {
        return mRunningTime;
    }

    public void setRunning_time(long running_time) {
        this.mRunningTime = running_time;
    }

    public long getBegin_time() {
        return mBeginTime;
    }

    public void setBegin_time(long begin_time) {
        this.mBeginTime = begin_time;
    }

    public long getEnd_time() {
        return mEndTime;
    }

    public void setEnd_time(long end_time) {
        this.mEndTime = end_time;
    }

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
