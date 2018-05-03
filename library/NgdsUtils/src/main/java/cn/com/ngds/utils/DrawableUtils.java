package cn.com.ngds.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;

/**
 * Created by ChenYuYun
 * 创建时间 17/2/6
 * 用于设置 TextView的图片
 */
public class DrawableUtils {
    public static final int RIGHT = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int TOP = 3;

    public static void setTextViewDrawable(Context context, TextView view, int imgId, int position) {
        Drawable img;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            img = context.getResources().getDrawable(imgId, null);
        } else {
            img = context.getResources().getDrawable(imgId);
        }
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        switch (position) {
            case RIGHT:
                view.setCompoundDrawables(null, null, img, null); //设置左图标
                break;
            case LEFT:
                view.setCompoundDrawables(img, null, null, null); //设置右边图标
                break;
            case BOTTOM:
                view.setCompoundDrawables(null, null, null, img); //设置bottom图标
                break;
            case TOP:
                view.setCompoundDrawables(null, img, null, null); //设置top图标
                break;

        }
    }

    public static void setTextViewNoDrawable(TextView view) {
        view.setCompoundDrawables(null, null, null, null); //设置左图标
    }

    public static void setTextViewDrawableRightAndTop(Context context, TextView view, int topImgId, int rightImgId) {
        Drawable topImg, rightImg;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            topImg = context.getResources().getDrawable(topImgId, null);
            rightImg = context.getResources().getDrawable(rightImgId, null);
        } else {
            topImg = context.getResources().getDrawable(topImgId);
            rightImg = context.getResources().getDrawable(rightImgId);
        }
        topImg.setBounds(0, 0, topImg.getMinimumWidth(), topImg.getMinimumHeight());
        rightImg.setBounds(0, 0, rightImg.getMinimumWidth(), rightImg.getMinimumHeight());
        view.setCompoundDrawables(null, topImg, rightImg, null); //设置左图标

    }

    public static Drawable getDrawable(Context context, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 把drawable渲染成某个颜色的图
     * @param drawable 目标drawable
     * @param color  最终渲染颜色
     * @return
     */
    public static Drawable tintDrawable(Drawable drawable, int color) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    /**
     * 把drawable渲染成某个颜色的图
     * @param drawable 目标drawable
     * @param colors  最终渲染ColorStateList
     * @return
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}
