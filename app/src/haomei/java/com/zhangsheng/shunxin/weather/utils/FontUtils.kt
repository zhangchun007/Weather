package com.zhangsheng.shunxin.weather.utils

import android.graphics.Typeface
import com.maiya.thirdlibrary.utils.AppContext
import java.util.*

class FontUtils private constructor() {
    val canGongFont: Typeface?
        get() {
            if (canGongTf == null) {
                canGongTf =
                    Typeface.createFromAsset(AppContext.getContext().assets, "fonts/cangong.ttf")
            }
            return canGongTf
        }
    val gOTHICFont: Typeface?
        get() {
            if (GOTHICTF == null) {
                GOTHICTF =
                    Typeface.createFromAsset(AppContext.getContext().assets, "fonts/gothic.ttf")
            }
            return GOTHICTF
        }
    val lunarFont: Typeface?
        get() {
            if (lunarTf == null) {
                lunarTf =
                    Typeface.createFromAsset(AppContext.getContext().assets, "fonts/lunar.ttf")
            }
            return lunarTf
        }
    val dINNumberFont: Typeface?
        get() {
            if (DINNumberTf == null) {
                DINNumberTf =
                    Typeface.createFromAsset(AppContext.getContext().assets, "fonts/DINNumber.ttf")
            }
            return DINNumberTf
        }

    companion object {
        @JvmStatic
        @Volatile
        var instance: FontUtils? = null
            get() {
                if (field == null) {
                    synchronized(FontUtils::class.java) {
                        if (field == null) {
                            field = FontUtils()
                        }
                    }
                }
                return field
            }
            private set
        private var canGongTf: Typeface? = null
        private var lunarTf: Typeface? = null
        private var DINNumberTf: Typeface? = null
        private var GOTHICTF: Typeface? = null
        var sActivityWhiteList = HashSet<String>()
    }
}