package cn.com.ngds.utils;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;


/**
 * AnimateUtils
 * Description:动画帮助类,方便统一管理
 * Author:walker_lee
 */
public class AnimateUtils {
    //获取小进度条旋转的动画
    public static Animation getSmallProgressRotateAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.loading);
    }

    /**
     * 控件由透明慢慢变成实物
     *
     * @param view
     */
    public static void viewTransparentToSolid(View view, int duration) {
        Animation myAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.transparent_to_solid);
        myAnimation.setDuration(duration);
        view.startAnimation(myAnimation);
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 控件由实物慢慢变成透明
     *
     * @param view
     */
    public static void viewTransparentFromSolid(View view, int duration) {
        Animation myAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.transparent_from_solid);
        myAnimation.setDuration(duration);
        view.startAnimation(myAnimation);
        view.setVisibility(View.GONE);
    }

    /**
     * 颜色渐变动画
     *
     * @param animateView
     * @param colorBegin
     * @param colorEnd
     * @param duration
     */
    public static void colorAnimate(final View animateView, int colorBegin, int colorEnd, int duration) {
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorBegin, colorEnd);
        colorAnimator.addUpdateListener(animation -> {
            int color = (int) animation.getAnimatedValue();
            animateView.setBackgroundColor(color);
        });
        colorAnimator.setDuration(duration);
        colorAnimator.start();
    }

    public static void alphaAnimate(final View animateView, @FloatRange(from = 0.0, to = 1.0) float alphaBegin,
                                    @FloatRange(from = 0.0, to = 1.0) float alphaEnd, int duration) {
        ValueAnimator alphaAnimator = ValueAnimator.ofObject(new FloatEvaluator(), alphaBegin, alphaEnd);
        alphaAnimator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            animateView.setAlpha(alpha);
        });
        alphaAnimator.setDuration(duration);
        alphaAnimator.start();
    }

    /**
     * 圆形消失动画
     *
     * @param animateView
     * @param x
     * @param y
     * @param duration
     */
    public static void hideCircularAnimate(final View animateView, int x, int y, int duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Animator animatorHide = ViewAnimationUtils
                    .createCircularReveal(animateView, x, y, (float) Math.hypot(animateView.getWidth(), animateView.getHeight()), 0);
            animatorHide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animateView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animatorHide.setDuration(duration);
            animatorHide.setInterpolator(new DecelerateInterpolator());
            animatorHide.start();
        } else {
            animateView.setVisibility(View.GONE);
        }
    }

    /**
     * 圆形显示动画
     *
     * @param animateView
     * @param x
     * @param y
     * @param duration
     */
    public static void showCircularAnimate(final View animateView, int x, int y, int duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Animator animator = ViewAnimationUtils.createCircularReveal(animateView, x, y, 0,
                    (float) Math.hypot(animateView.getWidth(), animateView.getHeight()));
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animateView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.setDuration(duration);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        } else {
            animateView.setVisibility(View.VISIBLE);
        }
    }
}
