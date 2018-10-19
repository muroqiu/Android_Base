package com.scrm.test.sqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scrm.test.sqlite.db.DbManager;
import com.scrm.test.sqlite.db.SnsInfo;

import java.security.MessageDigest;
import java.util.Arrays;
import java.nio.charset.Charset;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText edtTest;
    TextView txtTest;
    String strTest = "080010001800222038346230363537343535396638313065353536326634616534343765303031642A0208012A0208022A0208032A0208042A0208052A0208063800400748AB998BB3E72C5000600C680080010092010420002800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D5E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D4E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D6E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D3E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D6E290DE0545000000004800A201250D00007AC41500007AC41D00007AC42500007AC42802300038D5E290DE0545000000004800AA0100C00100C80100D80100";

    String strTest2 = "e4bda0e5a5bd";

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

    public static byte[] hexStrToByteArray(String str)
    {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++){
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
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
}
