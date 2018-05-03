package cn.com.ngds.utils;

import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;

/**
 * view相关工具类
 */
public class ViewUtils {

    /**
     * 测量view
     *
     * @param view
     */
    public static void measure(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }

    /**
     * 获取测量后的高度
     *
     * @param view
     * @return
     */
    public static int getHeight(View view) {
        return view.getMeasuredHeight();
    }

    /**
     * 获取测量后的宽度
     *
     * @param view
     * @return
     */
    public static int getWidth(View view) {
        return view.getMeasuredWidth();
    }

    /**
     * 设置可见性
     *
     * @param v
     * @param isVisible
     */
    public static void setVisible(View v, boolean isVisible) {
        if (v == null) {
            return;
        }
        v.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取可见性
     *
     * @param v
     * @return
     */
    public static boolean isVisible(View v) {
        return v != null && v.getVisibility() == View.VISIBLE;
    }

    /**
     * 根据id充气布局
     *
     * @param layoutId
     * @param parent
     * @return
     */
    public static View getView(@LayoutRes int layoutId, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    private static long lastClickTime;

    /**
     * 是否快速点击
     */
    public static boolean isQuickClick() {
        long time = System.currentTimeMillis();
        if (Math.abs(time - lastClickTime) < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 手指触摸的区域是否是在view上
     *
     * @param view 判断的view
     * @param x    x坐标
     * @param y    y坐标
     * @return true触摸在view上，false触摸位置在view之外
     */
    public static boolean isPinnedViewTouched(View view, float x, float y) {
        Rect touchRect = new Rect();
        touchRect.top = view.getTop();
        touchRect.bottom = view.getBottom();
        touchRect.left = view.getLeft();
        touchRect.right = view.getRight();
        return touchRect.contains((int) x, (int) y);
    }

    /**
     * view点击区域扩大工具
     * 注意：
     * 1、若View的自定义触摸范围超出Parent的大小，则超出的那部分无效。
     * 2、一个Parent只能设置一个View的TouchDelegate，设置多个时只有最后设置的生效。
     *
     * @param view    需要扩大的view
     * @param padding 上下左右扩大距离
     */
    public static void expandTouchAra(final View view, final int padding) {
        expandTouchAra(view, padding, padding, padding, padding);
    }

    /**
     * @param view   需要扩大的view
     * @param left   左边扩大距离
     * @param top    顶部扩大距离
     * @param right  右边扩大距离
     * @param bottom 底部扩大距离
     */
    public static void expandTouchAra(final View view, final int left, final int top, final int right, final int bottom) {
        if (view == null) {
            throw new IllegalStateException("view can't be null!");
        }
        final View parent = ((View) view.getParent());
        if (parent == null) {
            throw new IllegalStateException("parent is null!");
        }
        parent.post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();

                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.left -= left;
                bounds.top -= top;
                bounds.right += right;
                bounds.bottom += bottom;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
                if (View.class.isInstance(parent)) {
                    parent.setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**
     * 还原View的触摸和点击响应范围
     *
     * @param view
     */
    public static void restoreTouchAra(final View view) {
        if (view == null) {
            throw new IllegalStateException("view can't be null!");
        }
        final View parent = ((View) view.getParent());
        if (parent == null) {
            throw new IllegalStateException("parent is null!");
        }
        parent.post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                bounds.setEmpty();
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
                if (View.class.isInstance(parent)) {
                    parent.setTouchDelegate(touchDelegate);
                }
            }
        });
    }
}
