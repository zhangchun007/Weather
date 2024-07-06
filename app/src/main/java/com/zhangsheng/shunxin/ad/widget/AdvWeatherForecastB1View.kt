package com.zhangsheng.shunxin.ad.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.maiya.thirdlibrary.ext.isVisible
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.zhangsheng.shunxin.R

/*
*
* 大图广告:样式二
*
* */
class AdvWeatherForecastB1View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ExtAdMaterialView(context, attrs, defStyleAttr) {

    override fun getLayoutId(): Int {
        return R.layout.adv_weather_forecast_b1
    }

}