package com.scrm.test.sqlite.db;

/**
 *
 *
 * @author 慕容秋 (muroqiu@qq.com)
 * Create on 18/10/18
 */
public class SnsInfo extends BaseType {

    private String mUserName = "";

    private byte[] mContent;

    private byte[] mAttr;

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public byte[] getContent() {
        return mContent;
    }

    public void setContent(byte[] mContent) {
        this.mContent = mContent;
    }

    public byte[] getAttr() {
        return mAttr;
    }

    public void setAttr(byte[] mAttr) {
        this.mAttr = mAttr;
    }

}
