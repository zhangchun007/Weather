package com.maiya.thirdlibrary.utils

import android.content.Context

object DisplayUtil {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * （DisplayMetrics类中属性density）
     * @return
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * （DisplayMetrics类中属性density）
     * @return
     */
    fun dip2px(dipValue: Float): Int {
        val scale = AppContext.getContext().resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp( pxValue: Float): Int {
        val fontScale = AppContext.getContext().resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2px( spValue: Float): Int {
        val fontScale = AppContext.getContext().resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun getScreenWidth(): Int {
        val dm = AppContext.getContext().resources.displayMetrics
        return dm.widthPixels
    }

    fun getScreenHeight(): Int {
        val dm = AppContext.getContext().resources.displayMetrics
        return dm.heightPixels
    }



    fun getDisplayDensity(): Float {
        val dm = AppContext.getContext().resources.displayMetrics
        return dm.density
    }



}