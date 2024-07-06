package com.zhangsheng.shunxin.weather.utils

import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/5/30 16:22
 */
object CalculateUtil {


    fun colorEvaluate(fraction:Float, startValue:Int ,endValue:Int ): Int {
        val startA = (startValue shr 24 and 0xff) / 255.0f
        var startR = (startValue shr 16 and 0xff) / 255.0f
        var startG = (startValue shr 8 and 0xff) / 255.0f
        var startB = (startValue and 0xff) / 255.0f

        val endA = (endValue shr 24 and 0xff) / 255.0f
        var endR = (endValue shr 16 and 0xff) / 255.0f
        var endG = (endValue shr 8 and 0xff) / 255.0f
        var endB = (endValue and 0xff) / 255.0f

        // convert from sRGB to linear

        // convert from sRGB to linear
        startR = Math.pow(startR.toDouble(), 2.2).toFloat()
        startG = Math.pow(startG.toDouble(), 2.2).toFloat()
        startB = Math.pow(startB.toDouble(), 2.2).toFloat()

        endR = Math.pow(endR.toDouble(), 2.2).toFloat()
        endG = Math.pow(endG.toDouble(), 2.2).toFloat()
        endB = Math.pow(endB.toDouble(), 2.2).toFloat()

        // compute the interpolated color in linear space

        // compute the interpolated color in linear space
        var a = startA + fraction * (endA - startA)
        var r = startR + fraction * (endR - startR)
        var g = startG + fraction * (endG - startG)
        var b = startB + fraction * (endB - startB)

        // convert back to sRGB in the [0..255] range

        // convert back to sRGB in the [0..255] range
        a *= 255.0f
        r = r.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
        g = g.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
        b = b.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f

        return a.roundToInt() shl 24 or (r.roundToInt() shl 16) or (g.roundToInt() shl 8) or b.roundToInt()
    }
}