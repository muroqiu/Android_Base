package cn.com.ngds.utils;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by muroqiu on 15/2/4.
 */
public final class IoUtil {
    private static final char HEX_DIGITS[] = {
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F'
    };

    public static String bin2Hex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            sb.append(HEX_DIGITS[(aB & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[aB & 0x0f]);
        }
        return sb.toString();
    }


    public static String bin2HexForTest(byte[] deviceToken) {
        StringBuilder sb = new StringBuilder(deviceToken.length * 2);
        for (byte aDeviceToken : deviceToken) {
            sb.append("\\x");
            sb.append(HEX_DIGITS[(aDeviceToken & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[aDeviceToken & 0x0f]);
        }
        return sb.toString();
    }

    public static byte[] hex2bin(String hex) {
        int len = hex.length() >> 1;
        if (len > 0) {
            byte[] data = new byte[len];
            try {
                for (int i = 0, hPos = 0; i < len; i++, hPos = i << 1) {
                    data[i] = (byte) Integer.parseInt(hex.substring(hPos, hPos + 2), 16);
                }
            } catch (NumberFormatException e) {
                return null;
            }
            return data;
        }
        return null;
    }

    public static byte[] variableLength(int length) {
        int resLength = 0;
        int result = 0;
        do {
            result |= (length & 0x7F) << 24;
            length >>>= 7;
            resLength++;
            if (length > 0) {
                result >>>= 8;
                result |= 0x80000000;
            }
        }
        while (length > 0);
        byte[] res = new byte[resLength];
        for (int i = 0, move = 24; i < resLength; i++) {
            res[i] = (byte) (result >>> move);
            move -= 8;
        }
        return res;
    }

    public static int readVariableLength(InputStream is) {
        int length = 0;
        int cur;
        try {
            do {
                cur = is.read();
                length |= (cur & 0x7F);
                if ((cur & 0x80) != 0)
                    length <<= 7;
            }
            while ((cur & 0x80) != 0);
            return length;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int readVariableLength(ByteBuffer buf) {
        int length = 0;
        int cur;
        if (buf.hasRemaining())
            do {
                cur = buf.get();
                length |= (cur & 0x7F);
                if ((cur & 0x80) != 0)
                    length <<= 7;
            }
            while ((cur & 0x80) != 0 && buf.hasRemaining());
        return length;
    }

    public static int writeLong(long v, byte[] b, int off) {
        b[off] = (byte) (0xFF & (v >> 56));
        b[off + 1] = (byte) (0xFF & (v >> 48));
        b[off + 2] = (byte) (0xFF & (v >> 40));
        b[off + 3] = (byte) (0xFF & (v >> 32));
        b[off + 4] = (byte) (0xFF & (v >> 24));
        b[off + 5] = (byte) (0xFF & (v >> 16));
        b[off + 6] = (byte) (0xFF & (v >> 8));
        b[off + 7] = (byte) (0xFF & v);

        return 8;
    }

    public static int writeMac(long v, byte[] b, int off) {
        b[off] = (byte) (0xFF & (v >> 40));
        b[off + 1] = (byte) (0xFF & (v >> 32));
        b[off + 2] = (byte) (0xFF & (v >> 24));
        b[off + 3] = (byte) (0xFF & (v >> 16));
        b[off + 4] = (byte) (0xFF & (v >> 8));
        b[off + 5] = (byte) (0xFF & v);
        return 6;
    }

    public static int writeInt(int v, byte[] b, int off) {
        b[off] = (byte) (0xFF & (v >> 24));
        b[off + 1] = (byte) (0xFF & (v >> 16));
        b[off + 2] = (byte) (0xFF & (v >> 8));
        b[off + 3] = (byte) (0xFF & v);
        return 4;
    }

    public static int writeShort(int v, byte[] b, int off) {
        b[off] = (byte) (0xFF & (v >> 8));
        b[off + 1] = (byte) (0xFF & v);
        return 2;
    }

    public static int writeByte(int v, byte[] b, int off) {
        b[off] = (byte) v;
        return 1;
    }

    public static int write(byte[] v, int src_off, byte[] b, int off, int len) {
        if (v == null || v.length == 0)
            return 0;
        System.arraycopy(v, src_off, b, off, len);
        return len;
    }

    public static short readShort(byte[] src, int off) {
        return (short) ((src[off] & 0xFF) << 8 | (src[off + 1] & 0xFF));
    }

    public static int readUnsignedShort(byte[] src, int off) {
        return ((src[off] & 0xFF) << 8 | (src[off + 1] & 0xFF));
    }

    public static int readInt(byte[] src, int off) {
        return (src[off] & 0xFF) << 24 | (src[off + 1] & 0xFF) << 16 | (src[off + 2] & 0xFF) << 8
                | (src[off + 3] & 0xFF);
    }

    public static long readLong(byte[] src, int off) {
        return (src[off] & 0xFFL) << 56 | (src[off + 1] & 0xFFL) << 48
                | (src[off + 2] & 0xFFL) << 40 | (src[off + 3] & 0xFFL) << 32
                | (src[off + 4] & 0xFFL) << 24 | (src[off + 5] & 0xFFL) << 16
                | (src[off + 6] & 0xFFL) << 8 | (src[off + 7] & 0xFFL);
    }

    public static long readMac(byte[] src, int off) {
        return (src[off] & 0xFFL) << 40 | (src[off + 1] & 0xFFL) << 32
                | (src[off + 2] & 0xFFL) << 24 | (src[off + 3] & 0xFFL) << 16
                | (src[off + 4] & 0xFFL) << 8 | (src[off + 5] & 0xFFL);
    }

    public static String replace(String from, String to, String source) {
        if (source == null || from == null || to == null)
            return null;
        if (!source.contains(from))
            return source;
        StringBuffer bf = new StringBuffer();
        int index = -1;
        while ((index = source.indexOf(from)) != -1) {
            bf.append(source.substring(0, index)).append(to);
            source = source.substring(index + from.length());
            index = -1;
        }
        bf.append(source);
        return bf.toString();
    }

    public static String[] splitString(String src, String regular, int limit) {
        return src.split(regular, limit);
    }

    public static boolean isNumberic(String src) {
        if (src == null || (src = src.trim()).equals(""))
            return false;
        char[] chars = src.toCharArray();
        boolean result = true;
        for (char c : chars) {
            if (c < '0' || c > '9')
                return false;
        }
        return result;
    }


    public final static int PROTOCOL = 0, HOST = 1, PORT = 2, PATH = 3, CONTENT = 4, ARGUMENTS = 5;
    public final static int READ = 1;
    public final static int WRITE = 1 << 1;
    public final static int READ_WRITE = READ | WRITE;

    public static String[] splitURL(String url) {
        StringBuffer u = new StringBuffer(url.toLowerCase());
        String[] result = new String[6];
        for (int i = 0; i < 6; i++) {
            result[i] = "";
        }
        // get protocol

        int index = url.indexOf(":");
        if (index > 0) {
            result[PROTOCOL] = url.substring(0, index);
            u.delete(0, index + 1);
        } else if (index == 0)
            throw new IllegalArgumentException("url format error - protocol");
        // check for host/port
        if (u.length() >= 2 && u.charAt(0) == '/' && u.charAt(1) == '/') {
            // found domain part
            u.delete(0, 2);
            int slash = u.toString().indexOf('/');
            if (slash < 0) {
                slash = u.length();
            }
            if (slash != 0) {
                int colon = u.toString().indexOf(':');
                int endIndex = slash;
                if (colon >= 0) {
                    if (colon > slash)
                        throw new IllegalArgumentException("url format error - port");
                    endIndex = colon;
                    result[PORT] = u.toString().substring(colon + 1, slash);
                }
                result[HOST] = u.toString().substring(0, endIndex);
                u.delete(0, slash);
            }
        }
        if (u.length() > 0) {
            url = u.toString();
            int slash = url.lastIndexOf('/');
            if (slash > 0)
                result[PATH] = url.substring(0, slash);
            else if (slash == 0) {
                if (url.indexOf('?') > 0)
                    throw new IllegalArgumentException("url format error - path");
                result[PATH] = url;
                return result;
            }
            if (slash < url.length() - 1) {
                String fn = url.substring(slash + 1, url.length());
                int anchorIndex = fn.indexOf('?');
                if (anchorIndex >= 0) {
                    result[CONTENT] = fn.substring(0, anchorIndex);
                    result[ARGUMENTS] = fn.substring(anchorIndex + 1);
                } else {
                    result[CONTENT] = fn;
                }
            }
        } else
            result[PATH] = "/";
        return result;
    }

    public static String mergeURL(String[] splits) {
        StringBuilder buffer = new StringBuilder();
        if (!splits[PROTOCOL].equals(""))
            buffer.append(splits[PROTOCOL]).append("://");
        if (!splits[HOST].equals(""))
            buffer.append(splits[HOST]);
        if (!splits[PORT].equals(""))
            buffer.append(':').append(splits[PORT]);
        if (!splits[PATH].equals("")) {
            buffer.append(splits[PATH]);
            if (!splits[PATH].equals("/"))
                buffer.append('/');
        }
        if (!splits[CONTENT].equals(""))
            buffer.append(splits[CONTENT]);
        if (!splits[ARGUMENTS].equals(""))
            buffer.append('?').append(splits[ARGUMENTS]);
        return buffer.toString();
    }

    public static String mergeProxyTarURL(String[] splits) {
        StringBuilder buffer = new StringBuilder();
        if (!splits[HOST].equals(""))
            buffer.append(splits[HOST]);
        if (!splits[PORT].equals(""))
            buffer.append(':').append(splits[PORT]);
        if (!splits[PATH].equals("")) {
            buffer.append(splits[PATH]);
            if (!splits[PATH].equals("/"))
                buffer.append('/');
        }
        if (!splits[CONTENT].equals(""))
            buffer.append(splits[CONTENT]);
        if (!splits[ARGUMENTS].equals(""))
            buffer.append('?').append(splits[ARGUMENTS]);
        return buffer.toString();
    }

    public static String mergeX_SerLet(String[] splits) {
        StringBuilder buffer = new StringBuilder();
        if (!splits[PATH].equals("")) {
            buffer.append(splits[PATH]);
            if (!splits[PATH].equals("/"))
                buffer.append('/');
        }
        if (!splits[CONTENT].equals(""))
            buffer.append(splits[CONTENT]);
        if (!splits[ARGUMENTS].equals(""))
            buffer.append('?').append(splits[ARGUMENTS]);
        return buffer.toString();
    }

    public static String mergeX_Host(String[] splits) {
        StringBuilder buffer = new StringBuilder();
        if (!splits[HOST].equals(""))
            buffer.append(splits[HOST]);
        if (!splits[PORT].equals(""))
            buffer.append(':').append(splits[PORT]);
        return buffer.toString();
    }
}
