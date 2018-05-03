package cn.com.ngds.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * 键盘工具类
 */
public class InputUtils {
    public static void observeInput(Activity activity, final InputChangeListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                int keyboardHeight = height - displayHeight - rect.top;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    listener.OnInputChange(keyboardHeight, !hide);
                }

                previousKeyboardHeight = height;

            }
        });
    }

    public interface InputChangeListener {
        void OnInputChange(int softKeyboardHeight, boolean visible);
    }

    /**
     * 隐藏输入法
     *
     * @param activity
     */
    public static void hideInput(Activity activity) {
        try {
            if (activity.getCurrentFocus() != null) {
                // 是否存在焦点
                InputMethodManager inputManager = (InputMethodManager) activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager == null) {
                    return;
                }
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输入法
     *
     * @param editText
     */
    public static void hideInput(EditText editText) {
        try {
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getApplicationContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager == null) {
                return;
            }
            inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0); //隐藏
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示输入法
     *
     * @param context
     */
    public static void showInput(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager == null) {
                return;
            }
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示输入法
     *
     * @param activity
     * @param view
     */
    public static void showInput(Context activity, View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager == null) {
                return;
            }
            view.requestFocus();
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param destContext 上下文对象
     *                    用于解决输入法内存泄露
     *                    参考：http://blog.csdn.net/sodino/article/details/32188809
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        try {
            for (String param : arr) {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
