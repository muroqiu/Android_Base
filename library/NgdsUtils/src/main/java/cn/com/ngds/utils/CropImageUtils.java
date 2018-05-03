package cn.com.ngds.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by wangyt on 2017/11/23.
 * : 剪切️图片帮助类
 */

public class CropImageUtils {
    // 设置intent处理类型
    private static final String IMAGE_UNSPECIFIED = "image/*";
    // 剪切图片操作
    private static final String ACTION_CROP = "com.android.camera.action.CROP";

    private static Intent getCaptureWithFileIntent(File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

    /**
     * 调用系统拍照功能
     *
     * @param activity    调用界面
     * @param file        拍照文件保存位置
     * @param requestCode 请求码
     */
    public static void captureWithFile(Activity activity, File file, int requestCode) {
        activity.startActivityForResult(getCaptureWithFileIntent(file), requestCode);
    }

    /**
     * 调用系统拍照功能
     *
     * @param fragment    调用界面
     * @param file        拍照文件保存位置
     * @param requestCode 请求码
     */
    public static void captureWithFile(Fragment fragment, File file, int requestCode) {
        fragment.startActivityForResult(getCaptureWithFileIntent(file), requestCode);
    }

    public static void captureWithFile(android.support.v4.app.Fragment fragment, File file, int requestCode) {
        fragment.startActivityForResult(getCaptureWithFileIntent(file), requestCode);
    }

    /**
     * 调用系统拍照功能
     * 以该方式调用时，图片保存在onActivityResult方法的Intent data参数中，可通过以下方式取回
     * Bitmap photo = data.getExtras().getParcelable("data");
     *
     * @param activity    调用界面
     * @param requestCode 请求码
     */
    public static void captureImage(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, requestCode);
    }

    private static Intent getPickAndCropImageIntent(File file) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        //        // aspectX aspectY 是宽高的比例
        ////        intent.putExtra("aspectX", 1);
        ////        intent.putExtra("aspectY", 1);
        //        // outputX outputY 是裁剪图片宽高
        ////        intent.putExtra("outputX", 64);
        ////        intent.putExtra("outputY", 64);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");
        //        intent.putExtra("return-data", "true");
        return intent;
    }

    /**
     * 获取本地图片，并做剪裁
     *
     * @param activity    调用界面
     * @param file        剪裁文件保存位置
     * @param requestCode 请求码
     */
    public static void pickAndCropImage(Activity activity, File file, int requestCode) {
        activity.startActivityForResult(getPickAndCropImageIntent(file), requestCode);
        // 可自定义选择框
        //        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"),
        //            requestCode);
    }

    /**
     * 获取本地图片，并做剪裁
     *
     * @param fragment    调用界面
     * @param file        剪裁文件保存位置
     * @param requestCode 请求码
     */
    public static void pickAndCropImage(Fragment fragment, File file, int requestCode) {
        fragment.startActivityForResult(getPickAndCropImageIntent(file), requestCode);
        // 可自定义选择框
        //        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"),
        //            requestCode);
    }

    private static Intent getPickImageIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(IMAGE_UNSPECIFIED);
        return intent;
    }

    /**
     * 获取本地图片
     * 以该方式调用时，图片保存在onActivityResult方法的Intent data参数中，可通过以下方式取回
     * Uri uri = data.getData();
     *
     * @param activity    调用界面
     * @param requestCode 请求码
     */
    public static void pickImage(Activity activity, int requestCode) {
        activity.startActivityForResult(getPickImageIntent(), requestCode);
    }

    /**
     * 获取本地图片
     * 以该方式调用时，图片保存在onActivityResult方法的Intent data参数中，可通过以下方式取回
     * Uri uri = data.getData();
     *
     * @param fragment    调用界面
     * @param requestCode 请求码
     */
    public static void pickImage(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getPickImageIntent(), requestCode);
    }

    public static void pickImage(android.support.v4.app.Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getPickImageIntent(), requestCode);
    }

    private static Intent getCropImageIntent(File in, File out) {
        Intent intent = new Intent(ACTION_CROP);
        intent.setDataAndType(Uri.fromFile(in), IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        //        intent.putExtra("aspectX", 1);
        //        intent.putExtra("aspectY", 1);
        //        // outputX outputY 是裁剪图片宽高
        //        intent.putExtra("outputX", 64);
        //        intent.putExtra("outputY", 64);
        //        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
        intent.putExtra("outputFormat", "JPEG");
        return intent;
    }

    /**
     * 裁剪图片
     *
     * @param activity    调用界面
     * @param in          要裁剪的图片
     * @param out         裁剪后的保存位置
     * @param requestCode 请求码
     */
    public static void cropImage(Activity activity, File in, File out, int requestCode) {
        activity.startActivityForResult(getCropImageIntent(in, out), requestCode);
    }

    /**
     * 裁剪图片
     *
     * @param fragment    调用界面
     * @param in          要裁剪的图片
     * @param out         裁剪后的保存位置
     * @param requestCode 请求码
     */
    public static void cropImage(android.support.v4.app.Fragment fragment, File in, File out, int requestCode) {
        fragment.startActivityForResult(getCropImageIntent(in, out), requestCode);
    }

}
