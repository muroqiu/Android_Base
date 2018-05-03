package cn.com.ngds.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by muroqiu on 15/2/4.
 */
public class DeviceUtil {
    private final static String DEVICE_ID = "device_id";
    private static MessageDigest MD5 = create("MD5");
    private static MessageDigest SHA_1 = create("SHA-1");
    private static MessageDigest SHA_256 = create("SHA-256");

    /**
     * 读取设备标识
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        if (null == context) {
            return null;
        }
        String deviceId = ConfigHelper.getInstance(context).loadKey(DEVICE_ID);
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = createDid(context);
            if (!TextUtils.isEmpty(deviceId)) {
                ConfigHelper.getInstance(context).saveKey(DEVICE_ID, deviceId);
            }
        }
        return deviceId;
    }

    /**
     * 根据一定规整生成device id
     *
     * @param ctx
     * @return
     */
    private static String createDid(Context ctx) {
        Random r = new Random();
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String wifi_mac_s = initWifiMac(wifiManager);
        int retry = 0;
        while (wifi_mac_s == null && retry++ < 10) {
            SystemClock.sleep(1000);
            wifi_mac_s = getWifiMac(wifiManager);
        }
        long wifi_mac = 0x4DE15247A89FL + (r.nextInt() % 4);

        try {
            if (wifi_mac_s != null) {
                wifi_mac_s = wifi_mac_s.replaceAll(":", "");
                wifi_mac = Long.parseLong(wifi_mac_s, 16);
            }
        } catch (Exception e) {

        }
        long bluetooth_mac = 0xEDEB52771D03L + 0;
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId() == null ? "" : tm.getSubscriberId();
        String imei = tm.getDeviceId() == null ? "" : tm.getDeviceId();
        imei = toIMEI(imei);
        StringBuilder sb = new StringBuilder();
        byte[] bytes = new byte[0];
        try {
            sb.append(wifi_mac).append(bluetooth_mac).append(imsi).append(imei);
            bytes = IoUtil.hex2bin(sb.toString());
        } catch (Exception e) {
            sb.append(wifi_mac).append(bluetooth_mac);
            bytes = IoUtil.hex2bin(sb.toString());
        }

        return IoUtil.bin2Hex(sha256(bytes));
    }

    public static String getWifiMac(WifiManager manager) {
        String mac = manager.getConnectionInfo().getMacAddress();
        return mac;
    }

    public static String initWifiMac(WifiManager manager) {
        String mac = manager.getConnectionInfo().getMacAddress();
        if (mac == null && !manager.isWifiEnabled()) {
            manager.setWifiEnabled(true);
            manager.setWifiEnabled(false);
            mac = manager.getConnectionInfo().getMacAddress();
        }
        return mac;
    }

    private static byte[] sha256(byte[] input) {
        return digest("SHA-256", input);
    }

    private static byte[] digest(String digestName, byte[] input) {
        return digest(digestName, input, 0);
    }

    private static byte[] digest(String digestName, byte[] input, int offset) {
        if (input == null || digestName == null)
            throw new NullPointerException();
        int len = input.length;
        if (len <= 0 || offset < 0 || offset >= len)
            throw new ArrayIndexOutOfBoundsException();
        MessageDigest md;
        String uName = digestName.toUpperCase();
        if ("MD5".equals(uName))
            md = MD5;
        else if ("SHA-1".equals(uName))
            md = SHA_1;
        else if ("SHA-256".equals(uName))
            md = SHA_256;
        else
            throw new IllegalArgumentException();
        md.reset();
        md.update(input, offset, len);
        return md.digest();
    }

    private static MessageDigest create(String name) {
        try {
            return MessageDigest.getInstance(name);
        } catch (NoSuchAlgorithmException ne) {
            return null;
        }
    }

    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId() == null ? "" : tm.getDeviceId();
        return imei;
    }

    /**
     * 将imei中大于16进制的字符转为0
     *
     * @param imei
     * @return
     */
    public static String toIMEI(String imei) {
        if (TextUtils.isEmpty(imei)) {
            return "";
        }
        imei = imei.toUpperCase();
        StringBuilder sbu = new StringBuilder();
        char[] chars = imei.toCharArray();
        for (char charInt : chars) {
            if ((charInt >= 48 && charInt <= 57) || (charInt >= 97 && charInt <= 102)) {
                sbu.append(charInt);
            } else {
                sbu.append("0");
            }
        }
        return sbu.toString();
    }

    /**
     * 保密手机号
     *
     * @param mobile
     * @return
     */
    public static String formatSecretMobile(String mobile) {
        String result = mobile;
        if (!TextUtils.isEmpty(mobile) && mobile.length() > 8) {
            result = mobile.substring(0, 3) + "****" + mobile.substring(7);
        }
        return result;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }
}
