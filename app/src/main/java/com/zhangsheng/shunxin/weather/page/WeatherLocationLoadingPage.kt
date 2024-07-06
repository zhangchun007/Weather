package com.zhangsheng.shunxin.weather.page

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.maiya.thirdlibrary.widget.BaseLoadingView
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.PageWeatherLocationLoadingBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.showReport

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/1/6 23:19
 */
class WeatherLocationLoadingPage @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseLoadingView(context, attrs, defStyleAttr) {
    private val binding=PageWeatherLocationLoadingBinding.inflate(LayoutInflater.from(context),this,true)

    companion object {
        val LOCATION_LOADING = "0"
        val LOCATION_FAIL = "-1"
        val LOCATION_SUCCESS = "2"
    }

    private var pageStatus = LOCATION_LOADING

    init {
        Glide.with(binding.loading).asGif().load(R.drawable.location_loading).into(binding.loading)
    }


    override fun setViewStatus(status: String) {
        super.setViewStatus(status)
        if (pageStatus != status) {
            pageStatus = status
            when (status) {
                LOCATION_FAIL -> {
                    binding.loading.postDelayed(Runnable {
                        showReport(EnumType.上报埋点.首次安装定位失败页面)
                        binding.loading.setImageResource(R.mipmap.icon_location_fail)
                    }, 1000)
                }
                LOCATION_SUCCESS -> {
                    binding.loading.postDelayed(Runnable {
                        binding.loading.setImageResource(R.mipmap.icon_weather_location_page_ok)
                    }, 1000)
                }
            }
        }
    }
}