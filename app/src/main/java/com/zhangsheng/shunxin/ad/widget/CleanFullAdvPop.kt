package com.zhangsheng.shunxin.ad.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.maiya.thirdlibrary.ext.*
import com.my.business.imp.plu.IPluViewListener
import com.zhangsheng.shunxin.databinding.LayoutCleanFullPopBinding
import com.zhangsheng.shunxin.databinding.LayoutWeatherRainSnowForecastBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.net.ReCodeUtils
import com.zhangsheng.shunxin.weather.net.bean.WeatherRainSnowForecastBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CleanFullAdvPop @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val binding =
        LayoutCleanFullPopBinding.inflate(LayoutInflater.from(context), this, true)



    fun getAdvLayout():ViewGroup{
        return binding.exLayout
    }

    fun getCloseView(): View {
        return binding.closeView
    }

}