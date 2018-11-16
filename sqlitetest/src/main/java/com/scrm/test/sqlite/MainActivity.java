package com.scrm.test.sqlite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scrm.test.sqlite.db.DbManager;
import com.scrm.test.sqlite.db.SnsInfo;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.security.MessageDigest;
import java.util.Arrays;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    EditText edtTest;
    TextView txtTest;
    String strTest = "080010001800222038346230363537343535396638313065353536326634616534343765303031642A0208012A0208022A0208032A0208042A0208052A0208063800400748AB998BB3E72C5000600C680080010092010420002800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D5E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D4E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D6E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D3E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D6E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D5E290DE0545000000004800AA0100C00100C80100D80100";

    String strTest2 = "e4bda0e5a5bd";
    private String mCurrApkPath = "/sdcard/com.scrm.robot.plus/enmicromsg20181113T152341.db";
//    private String mCurrApkPath = "/sdcard/com.scrm.robot.plus/EnMicroMsg.db";
    private String mUin = "1041636089";
    // 867251035344598   91c1892

    private static String imei;
    private static String imei2;
    private static String meid;
    private static String sSerialInfo;
    private static Vector<String> imeiLst = new Vector<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTest = (EditText) findViewById(R.id.edtTest);
        txtTest = (TextView) findViewById(R.id.txtTest);

        edtTest.setText(strTest);

    }

//
//    private static boolean bwv() {
//        g.buY();
//        a aVar = a.xzb;
//        i iVar = new i();
//        try {
//            com.tencent.mm.kernel.g.Dr();
//            com.tencent.mm.kernel.g.Dq().Db().a(aVar, new String(iVar.toByteArray(), Charset.forName("ISO-8859-1")));
//        } catch (IOException e) {
//            x.w("MicroMsg.RedDotUtil", "mardRedotList save exception:" + e.getLocalizedMessage());
//        }
//        return false;
//    }
//
//    protected static byte[] aS(String str) {
//        try {
//            return str.getBytes("ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            throw new AssertionError(e);
//        }
//    }
//

    public static byte[] hexStrToByteArray(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }

    public void testByte() {
        String a;
//        a.getBytes()
        byte[] b1 = new byte[2];
        new String(b1, Charset.forName("ISO-8859-1"));
    }

    public static final String snsDecode(byte[] bArr) {
        int i = 0;
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bArr);
            byte[] digest = instance.digest();
            int length = digest.length;
            char[] cArr2 = new char[(length * 2)];
            int i2 = 0;
            while (i < length) {
                byte b = digest[i];
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                i2 = i3 + 1;
                cArr2[i3] = cArr[b & 15];
                i++;
            }
            return new String(cArr2);
        } catch (Exception e) {
            return null;
        }
    }

    public void onBtnTestClick(View view) {
        Log.e("TT", "onBtnTestClick");

        Log.e("strTest", snsDecode(hexStrToByteArray(strTest)));
        Log.e("strTest2", snsDecode(hexStrToByteArray(strTest2)));

//        printLog(Base64.decode(strTest, 10));
//        printLog(Base64.decode(strTest2, 0));
//        printLog(Base64.decode(strTest, 11));
//        printLog(Base64.decode(strTest2, 2));

//        printLog(Base64.decode(hexStrToByteArray(strTest), 10));
//        printLog(Base64.decode(hexStrToByteArray(strTest2), 0));
//
//        printLog(Base64.decode(hexStrToByteArray(strTest), 11));
//        printLog(Base64.decode(hexStrToByteArray(strTest2), 2));

        DbManager db = DbManager.getInstance(this, "");

        List<SnsInfo> snsInfoList = db.getSnsInfoList();

        for (SnsInfo snsInfo : snsInfoList) {
            Log.e("getContent", snsDecode(snsInfo.getContent()));
            Log.e("getAttr", snsDecode(snsInfo.getAttr()));
//            printLog(Base64.decode(Arrays.copyOfRange(snsInfo.getContent(), 2, snsInfo.getContent().length), 0));
//            printLog(Base64.decode(Arrays.copyOfRange(snsInfo.getAttr(), 2, snsInfo.getAttr().length), 0));
//            printLog(Base64.decode(Arrays.copyOfRange(snsInfo.getContent(), 1, snsInfo.getContent().length), 2));
//            printLog(Base64.decode(Arrays.copyOfRange(snsInfo.getAttr(), 2, snsInfo.getAttr().length), 2));
        }

        txtTest.setText(new String(hexStrToByteArray(edtTest.getText().toString()), Charset.forName("ISO-8859-1")));

    }

    private void printLog(byte[] bytes) {
//        Base64.encodeToString(this.pOG.format(new Date()).getBytes(), 0)
//        String str = new String(Base64.decode(kVar.url, 0));
//
        Log.e("UTF-8", new String(bytes, Charset.forName("UTF-8")));
        Log.e("UTF-16", new String(bytes, Charset.forName("UTF-16")));
        Log.e("UTF-16BE", new String(bytes, Charset.forName("UTF-16BE")));
        Log.e("UTF-16LE", new String(bytes, Charset.forName("UTF-16LE")));
        Log.e("UTF-16", new String(bytes, Charset.forName("UTF-16")));
        Log.e("ISO-8859-1", new String(bytes, Charset.forName("ISO-8859-1")));
        Log.e("ISO-10646-UCS-2", new String(bytes, Charset.forName("ISO-10646-UCS-2")));
        Log.e("US-ASCII", new String(bytes, Charset.forName("US-ASCII")));
        Log.e("UTF8", new String(bytes, Charset.forName("UTF8")));
    }


    /**
     * 根据imei和uin生成的md5码，获取数据库的密码（去前七位的小写字母）
     *
     * @param imei
     * @param uin
     * @return
     */
    private String initDbPassword(String imei, String uin) {
        if (TextUtils.isEmpty(imei) || TextUtils.isEmpty(uin)) {
            Log.e("initDbPassword", "初始化数据库密码失败：imei或uid为空");
            return "";
        }
        String md5 = md5(imei + uin);
        String password = md5.substring(0, 7).toLowerCase();
        Log.e("initDbPassword", password);
        return password;
    }

    /**
     * md5加密
     *
     * @param content
     * @return
     */
    private String md5(String content) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(content.getBytes("UTF-8"));
            byte[] encryption = md5.digest();//加密
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    sb.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取手机的imei码
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    private String initPhoneIMEI() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        try {
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 连接数据库
     *
     * @param dbFile
     */
    private void openWxDb(File dbFile, String ii) throws Exception {
//        String imei = initPhoneIMEI();
        String mDbPassword = initDbPassword(ii, mUin);
        Log.e("openWxDb", ii + "   " + mDbPassword);
        Context context = this.getApplicationContext();
        SQLiteDatabase.loadLibs(context);
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;"); //兼容2.0的数据库
            }
        };

        try {
            //打开数据库连接
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, mDbPassword, null, hook);
            //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
            Cursor c1 = db.rawQuery("select * from rcontact where verifyFlag = 0 and type != 4 and type != 2 and nickname != '' limit 20, 9999", null);
            while (c1.moveToNext()) {
                String userName = c1.getString(c1.getColumnIndex("username"));
                String alias = c1.getString(c1.getColumnIndex("alias"));
                String nickName = c1.getString(c1.getColumnIndex("nickname"));

                Log.d("data", " userName " + userName + " alias " + alias + " nickName " + nickName);
            }
            c1.close();
            db.close();
        } catch (Exception e) {
            Log.e("openWxDb", "读取数据库信息失败" + e.toString());
            throw e;
        }
    }


    /**
     * 获得imei1 ,imei2 ,deviceId meid  Serialno;
     *
     * @param
     * @return
     */
    public void initPhoneIds() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {
                Log.d("initPhoneIds", "-Build.VERSION.SDK_INT =" + Build.VERSION.SDK_INT);
                TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    imei = manager.getImei(0);
                    meid = manager.getMeid();
                    if (manager.getPhoneCount() == 2) {
                        imei2 = manager.getImei(1);
                    }
                } else {
                    imei = manager.getDeviceId();
                    imei2 = manager.getDeviceId(1);
                    meid = manager.getDeviceId(2);
                }
                getSerialNumber();
                Log.e("imei", imei);
                Log.e("imei2", imei2);
                Log.e("meid", meid);
                Log.e("sSerialInfo", sSerialInfo);
                imeiLst.clear();
                imeiLst.add(imei);
                imeiLst.add(imei2);
                imeiLst.add(meid);
                imeiLst.add(sSerialInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取手机设备的序列号
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getSerialNumber() {
        if (TextUtils.isEmpty(sSerialInfo)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sSerialInfo = Build.getSerial();
            } else {
                sSerialInfo = Build.SERIAL;
            }
        }
        return sSerialInfo;
    }

    public void onBtnTestWXClick(View view) {
        File db = new File(mCurrApkPath);
        if (!db.exists()) {
            return;
        }
        Log.e("openWxDb", db.length() + " " + db.getTotalSpace());
        initPhoneIds();
        for (int i=0; i< imeiLst.size(); i++) {
            try {
                openWxDb(db, imeiLst.get(i));
                Log.e("imeiLst", i + " " + imeiLst.get(i));
                break;
            } catch (Exception e) {
                Log.e("openWxDb", e.toString());
            }
        }
    }
}
