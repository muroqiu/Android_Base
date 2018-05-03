package cn.com.ngds.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 图片处理工具
 * Created by ytzyg on 15/1/15.
 */
public final class ImageUtil {

    /**
     * 压缩图片
     *
     * @param image   图片文件
     * @param quality 压缩质量，百分数
     * @return
     * @throws IOException
     */
    public static File compressImage(Context context, File image, int quality) throws IOException {
        if (quality > 100) {
            quality = 100;
        } else if (quality < 1) {
            quality = 1;
        }
        File result = File.createTempFile("temp", ".jpg");
        FileOutputStream os = new FileOutputStream(result);
        Bitmap bitmap = decodeSampledBitmapFromResource(context, image.getAbsolutePath());
        //质量压缩方法，100表示不压缩，把压缩后的数据存放到输出流中
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
        os.close();
        return result;
    }

    public static Bitmap decodeSampledBitmapFromResource(Context context, String path) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        WindowManager wm = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据uri返回文件，
     *
     * @param context 调用页面
     * @param uri     uri可能是文件路径
     * @return
     */
    public static File getImageFileFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (uri.toString().startsWith("file")) {
            // 如果是本地文件，直接生成文件
            return new File(uri.getPath());
        } else if (uri.toString().startsWith("http")) {
            // 如果是网络文件，下载到本地，生成文件 TODO 异步任务，可能未成功下载
            return getImageFormHttp(context, uri.toString());
        } else {
            String[] prj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, prj, null, null, null);
            int imgColIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imgPath = cursor.getString(imgColIdx);
            cursor.close();
            return new File(imgPath);
        }
    }

    /**
     * 获取本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 下载JPG网络图片到本地
     *
     * @param context
     * @param url
     * @return
     */
    public static File getImageFormHttp(Context context, String url) {
        Bitmap bitmap = returnBitmap(url);

        File dir = FileUtils.getExternalCacheDir(context) != null ?
                FileUtils.getExternalCacheDir(context) :
                FileUtils.getInternalCacheDir(context);
        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        try {
            if (file.createNewFile()) {
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        return file;
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    private static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    /**
     * 计算大小
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}


