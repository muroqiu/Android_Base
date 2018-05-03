package cn.com.ngds.utils;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 对象工具
 */
public class ObjectUtils {

    private ObjectUtils() {
        throw new AssertionError();
    }

    /**
     * 判断数组是否为空
     *
     * @param data
     * @return
     */
    public static boolean isEmpty(Collection data) {
        return data == null || data.isEmpty();
    }

    /**
     * 判断数组是否为空
     *
     * @param data
     * @return
     */
    public static boolean isEmpty(Object[] data) {
        return data == null || data.length == 0;
    }

    /**
     * 合并去重两个list
     *
     * @param l1
     * @param l2
     * @return
     */
    public static <T extends Comparable<T>> List<T> mergeAndSort(List<T> l1, List<T> l2) {
        // 都不为空，且两者不同才要合并比较
        if (!isEmpty(l1) && isEmpty(l2) && l1 != l2) {
            Set<T> temp = new TreeSet<T>();
            temp.addAll(l1);
            temp.addAll(l2);
            return new ArrayList<T>(temp);
        }
        // 能运行到这说明至少有一个为空，返回不为空的即可
        if (!isEmpty(l1)) {
            return l1;
        }
        return l2;
    }

    /**
     * compare two object
     *
     * @param actual
     * @param expected
     * @return <ul>
     * <li>if both are null, return true</li>
     * <li>return actual.{@link Object#equals(Object)}</li>
     * </ul>
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    public static <T> List<T> distinct(List<T> list1, List<T> l2) {
        HashSet<T> set = new HashSet<>();
        List<T> result = new ArrayList<>();
        if (list1 == null) {
            list1 = new ArrayList<>();
        }
        if (l2 == null) {
            l2 = new ArrayList<>();
        }
        set.addAll(list1);
        for (T t : l2) {
            boolean b = set.add(t);
            if (!b) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * null Object to empty string
     * <p/>
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    /**
     * convert long array to Long array
     *
     * @param source
     * @return
     */
    public static Long[] transformLongArray(long[] source) {
        Long[] destin = new Long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert Long array to long array
     *
     * @param source
     * @return
     */
    public static long[] transformLongArray(Long[] source) {
        long[] destin = new long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert int array to Integer array
     *
     * @param source
     * @return
     */
    public static Integer[] transformIntArray(int[] source) {
        Integer[] destin = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert Integer array to int array
     *
     * @param source
     * @return
     */
    public static int[] transformIntArray(Integer[] source) {
        int[] destin = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * compare two object
     * <ul>
     * <strong>About result</strong>
     * <li>if v1 > v2, return 1</li>
     * <li>if v1 = v2, return 0</li>
     * <li>if v1 < v2, return -1</li>
     * </ul>
     * <ul>
     * <strong>About rule</strong>
     * <li>if v1 is null, v2 is null, then return 0</li>
     * <li>if v1 is null, v2 is not null, then return -1</li>
     * <li>if v1 is not null, v2 is null, then return 1</li>
     * <li>return v1.{@link Comparable#compareTo(Object)}</li>
     * </ul>
     *
     * @param v1
     * @param v2
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <V> int compare(V v1, V v2) {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable) v1).compareTo(v2));
    }

    /**
     * 序列化对象
     *
     * @return
     * @throws IOException
     */
    public static String serialize(Object info) {
        try {
//			long startTime = System.currentTimeMillis();
            if (info == null) return null;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(info);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
            objectOutputStream.close();
            byteArrayOutputStream.close();
//			long endTime = System.currentTimeMillis();
//			Loger.d("serial", "序列化耗时为:" + (endTime - startTime));
            return serStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deSerialization(String str) {
        Object object = null;
        try {
            if (!TextUtils.isEmpty(str)) {
                String redStr = java.net.URLDecoder.decode(str, "UTF-8");
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                object = objectInputStream.readObject();
                objectInputStream.close();
                byteArrayInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
