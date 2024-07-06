package com.zhangsheng.shunxin.weather.utils;

import android.content.Context;
import android.graphics.Typeface;


public class FontUtils {
    private FontUtils() {

    }

    private static volatile FontUtils instance;
    private static Typeface canGongTf;
    private static Typeface lunarTf;
    private static Typeface DINNumberTf;
    private static Typeface GOTHICTF;
    public static final int SMALL_FONT_SIZE = 1;
    public static final int NORMAL_FONT_SIZE = 2;
    public static final int BIG_FONT_SIZE = 3;
    public static final int SUPER_BIG_FONT_SIZE = 4;


    public static FontUtils getInstance() {
        if (instance == null) {
            synchronized (FontUtils.class) {
                if (instance == null) {
                    instance = new FontUtils();
                }
            }
        }
        return instance;
    }


    public Typeface getCanGongFont(Context context) {
        if (canGongTf == null) {
            canGongTf = Typeface.createFromAsset(context.getAssets(), "fonts/cangong.ttf");
        }
        return canGongTf;
    }

    public Typeface getGOTHICFont(Context context) {
        if (GOTHICTF == null) {
            GOTHICTF = Typeface.createFromAsset(context.getAssets(), "fonts/gothic.ttf");
        }
        return GOTHICTF;
    }

    public Typeface getLunarFont(Context context) {
        if (lunarTf == null) {
            lunarTf = Typeface.createFromAsset(context.getAssets(), "fonts/lunar.ttf");
        }
        return lunarTf;
    }

    public Typeface getDINNumberFont(Context context) {
        if (DINNumberTf == null) {
            DINNumberTf = Typeface.createFromAsset(context.getAssets(), "fonts/DINNumber.ttf");
        }
        return DINNumberTf;
    }

}
