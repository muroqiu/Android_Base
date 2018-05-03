package cn.com.ngds.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * 文件操作类
 * <p>
 * Created by ZhangWF(zhangwf0929@gmail.com) on 14-4-24.
 */
public class FileUtils {

    private static final String EXTERNAL_DIR = "/Android/data/";
    private static final String DATABASE = "database";
    private static final String CACHE = "cache";
    private static final String CRASH = "crash";
    private static final String TEMP = "temp";

    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return String.format("%32s", bigInt.toString(16)).replace(' ', '0');
    }

    public static String calculateMD5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    public static String getFileMd5Name(File file) {
        String fileMd5 = calculateMD5(file);
        String fileName = file.getName();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        return fileMd5 + prefix;
    }


    /**
     * 判断SD是否可用
     *
     * @return
     */
    public static boolean isExternalStorageEnable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 判断外部存储是否能被移除
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * 获取外部存储目录
     *
     * @return The external storage dir
     */
    public static File getSDCardDir() {
        if (!isExternalStorageEnable()) {
            return null;
        }
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 判断是否支持外部存储缓存
     *
     * @return
     */
    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * 获取指定目录的可用空间
     *
     * @param path 需要判断的路径，必须是目录
     * @return 可以用的字节数
     */
    public static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean checkFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 删除文件夹
     *
     * @param dir
     */
    public static boolean deleteDir(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return false;
        }
        // 要先删除目录下的所有文件及文件夹后，才能删除该目录
        File[] files = dir.listFiles();
        if (!ObjectUtils.isEmpty(files)) {
            boolean result;
            for (File file : files) {
                if (file.isFile()) {
                    // 文件的话，直接删除
                    result = file.delete();
                } else {
                    // 如果是目录，递归删除
                    result = deleteDir(file);
                }
                if (!result) {
                    // 如果删除失败，提前返回
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 创建文件以及它的所有上级目录
     *
     * @param file 可以是文件，也可以是目录
     * @return 创建成功返回true, 失败返回false
     */
    public static boolean createDirsAndFile(File file) {
        try {
            if (file.exists()) {
                return true;
            }
            if (file.isDirectory()) {
                file.mkdirs();
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!file.exists()) {
                    return file.createNewFile();
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 从InputStream拷贝文件到本地
     *
     * @param src  文件流
     * @param dest 目标文件，不能是目录
     * @return 是否成功，overlay为false并且文件已存在时也返回false
     */
    public static boolean copyFile(InputStream src, File dest, boolean overlay) {
        BufferedOutputStream out = null;
        try {
            // 需要指定的目标文件，不能使目录
            if (dest.isDirectory()) {
                return false;
            }
            if (dest.exists()) {
                if (overlay) {
                    dest.delete();
                } else {
                    return false;
                }
            } else {
                createDirsAndFile(dest);
            }
            out = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = src.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static File getFileFromCache(Context context, String fileName) {
        File intCacheDir = getInternalCacheDir(context);
        return getFileFromCache(context, fileName, intCacheDir);
    }

    public static File getFileFromCache(Context context, String fileName, File cacheDir) {
        if (context != null && cacheDir != null) {
            File cachedFile = new File(cacheDir, fileName);
            if (cachedFile.exists()) {
                return cachedFile;
            }
        }
        return null;
    }

    /**
     * 获取应用的外部缓存目录
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (!isExternalStorageEnable()) {
            return null;
        }
        File dir;
        if (hasExternalCacheDir()) {
            dir = context.getExternalCacheDir();
        } else {
            // Before Froyo we need to construct the external cache dir ourselves
            String path = EXTERNAL_DIR + context.getPackageName() + CACHE;
            dir = new File(Environment.getExternalStorageDirectory().getPath() + path);
        }
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取内部存储中app的目录
     *
     * @param context
     * @return
     */
    public static File getInternalCacheDir(Context context) {
        if (context != null) {
            File intCacheDir = context.getCacheDir();
            if (!intCacheDir.exists()) {
                intCacheDir.mkdirs();
            }
            return intCacheDir;
        }
        return null;
    }

    public static File addFileToCache(Context context, String fileName) {
        File intCacheDir = getInternalCacheDir(context);
        return addFileToCache(context, fileName, intCacheDir);
    }

    public static File addFileToCache(Context context, String fileName, File cacheDir) {
        if (context != null && cacheDir != null) {
            File cachedFile = new File(cacheDir, fileName);
            if (!cachedFile.exists()) {
                try {
                    cachedFile.createNewFile();
                } catch (IOException e) {
                }
            }
            return cachedFile;
        }
        return null;
    }

    /**
     * 通过后缀名筛选文件
     *
     * @param directory 需要扫描的目录
     * @param suffix    后缀名
     * @return 文件数组
     */
    public static File[] listFilesBySuffix(File directory, final String suffix) {
        return directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(suffix);
            }
        });
    }

    /**
     * 通过文件名筛选文件
     *
     * @param directory 需要扫描的目录
     * @param keyname   文件名
     * @return 文件数组
     */
    public static File[] listFilesByName(File directory, final String keyname) {
        return directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.indexOf(keyname) >= 0;
            }
        });
    }

    /**
     * 获取私有数据库目录，“data/data/包名/app_database”
     *
     * @param context 上下文环境
     * @return 目录文件
     */
    public static File getPrivateDatabaseDir(Context context) {
        return context.getApplicationContext().getDir(DATABASE, Context.MODE_PRIVATE);
    }

    /**
     * 获取私有缓存目录，“data/data/包名/app_cache”
     *
     * @param context 上下文环境
     * @return 目录文件
     */
    public static File getPrivateCacheDir(Context context) {
        return context.getApplicationContext().getDir(CACHE, Context.MODE_PRIVATE);
    }

    /**
     * 将drawable转为bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    /**
     * 将bmp转为byte
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 在指定目录查找文件
     *
     * @param dir
     * @param fileName
     * @return
     */
    public static File findFileInDir(File dir, String fileName) {
        if (!TextUtils.isEmpty(fileName) && dir != null && dir.exists()) {
            File[] subFile = dir.listFiles();
            for (File f : subFile) {
                String name = f.getName();
                if (name.trim().equalsIgnoreCase(fileName)) {
                    return f;
                }
            }
        }
        return null;
    }

    /**
     * 移动文件，覆盖方式
     *
     * @param srcFile 源文件完整路径
     * @param desDir  目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(File srcFile, File desDir) {
        if (srcFile == null || desDir == null || !srcFile.exists() || !srcFile.isFile()) {
            return false;
        }

        if (!desDir.exists()) {
            if (!desDir.mkdirs()) {
                // 如果创建失败
                return false;
            }
        }
        // 如果目标文件夹下存在同名文件，则删除
        File desFile = new File(desDir, srcFile.getName());
        if (desFile.exists()) {
            if (!desFile.delete()) {
                return false;
            }
        }

        return srcFile.renameTo(new File(desDir, srcFile.getName()));
    }

    /**
     * 移动目录
     *
     * @param srcDir 源目录完整路径
     * @param desDir 目的目录完整路径
     * @return 目录移动成功返回true，否则返回false
     */
    public static boolean moveDir(File srcDir, File desDir) {
        if (srcDir == null || desDir == null || !srcDir.exists() || !srcDir.isDirectory()) {
            return false;
        }

        if (!desDir.exists()) {
            if (!desDir.mkdirs()) {
                // 如果创建失败
                return false;
            }
        }

        // 在desDir下查找与srcDir同名的目录，如果没有则创建；这才是真正要转移的目录
        File targetDir = new File(desDir.getAbsolutePath() + File.separator + srcDir.getName());
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                // 如果创建失败
                return false;
            }
        }

        /**
         * 如果是文件则移动，否则递归移动文件夹。删除最终的空源文件夹
         * 注意移动文件夹时保持文件夹的树状结构
         */
        File[] sourceFiles = srcDir.listFiles();
        for (File sourceFile : sourceFiles) {
            boolean result = true;
            if (sourceFile.isFile()) {
                result = moveFile(sourceFile, targetDir);
            } else if (sourceFile.isDirectory()) {
                result = moveDir(sourceFile, targetDir);
            }
            if (!result) {
                return false;
            }
        }
        return deleteDir(srcDir);
    }
}
