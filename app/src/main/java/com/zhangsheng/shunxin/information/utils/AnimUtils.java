package com.zhangsheng.shunxin.information.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;


public class AnimUtils {

    public static void show(View view) {
//        ObjectAnimator translationX = new ObjectAnimator().ofFloat(view, "translationX", 0, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 50, 0);
//        AnimatorSet animatorSet = new AnimatorSet();
//        //组合动画
//        animatorSet.playTogether(translationX, translationY);
//        //设置动画
        translationY.setDuration(300);
        // 设置动画时间
        translationY.start(); //启动
    }

    public static void hide(View view) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0, 50);
//        //设置动画
        translationY.setDuration(200);
        // 设置动画时间
        translationY.start(); //启动
    }

    public static void hideTips(View view) {
        int height = view.getHeight();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0, -height);
//        //设置动画
        translationY.setDuration(500);
        // 设置动画时间
        translationY.start(); //启动
    }

    public static void showTips(View view, Context context) {
        int height = view.getHeight();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", -DensityUtil.dip2px(context,30), 0);
//        //设置动画
        translationY.setDuration(500);
        // 设置动画时间
        translationY.start(); //启动
    }


    public static void dissmissTips(View view,Context context) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0, -DensityUtil.dip2px(context,30));
//        //设置动画
        translationY.setDuration(200);
        // 设置动画时间
        translationY.start(); //启动
        translationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
    }



}
