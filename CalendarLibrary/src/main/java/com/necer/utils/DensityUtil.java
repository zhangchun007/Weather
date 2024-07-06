package com.necer.utils;

import android.content.res.Resources;

/**
 * @Description:
 * @Author: lhy
 * @CreateDate: 2020/4/21 11:23
 */
public class DensityUtil {

    private static float density;

    static {
        density = Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * 将dip或dp值转换为px值
     *
     * @param dip
     * @return
     */
    public static int dp2px(int dip) {
        return (int) (dip * density + 0.5);
    }
}
