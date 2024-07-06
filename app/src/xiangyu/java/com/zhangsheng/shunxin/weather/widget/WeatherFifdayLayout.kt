package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.LayoutFifWeatherDayBinding
import com.zhangsheng.shunxin.weather.adapter.FifWeatherAdapter
import com.zhangsheng.shunxin.weather.adapter.FifWeatherListAdapter
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean

class WeatherFifdayLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var isFifLoadMore=false
    var weatherData:WeatherBean?=null
    val binding=LayoutFifWeatherDayBinding.inflate(LayoutInflater.from(context),this,true)
    init {

        initListener()
        binding.fifListWeather.setSmartListener(FifWeatherListAdapter())
        binding.fifWeather.setSmartListener(FifWeatherAdapter())
    }

    private fun initListener() {
        binding.linChartType.setSingleClickListener {
            changeFifChartState(true)
        }

        binding.linLineType.setSingleClickListener {
            changeFifChartState(false)
        }

        binding.fifLoadMore.setSingleClickListener {
            if (!isFifLoadMore) {
                isFifLoadMore = true
                binding.fifListWeather.notifyData(weatherData.nN().ybds.nN())
                binding.fifLoadMore.text = "点击收起"
                binding.fifLoadMore.setImageBackGround(
                    R.mipmap.icon_arrow_up,
                    binding.fifLoadMore.RIGHT_DRAWABLE
                )
            } else {
                if (weatherData.nN().ybds.nN().size <= 7) return@setSingleClickListener
                isFifLoadMore = false
                binding.fifListWeather.notifyData(weatherData.nN().ybds.nN().subList(0, 7))
                binding.fifLoadMore.text = "点击查看15天天气"
                binding.fifLoadMore.setImageBackGround(
                    R.mipmap.icon_arrow_down,
                    binding.fifLoadMore.RIGHT_DRAWABLE
                )
            }
        }

        binding.fifWeather.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    clickReport(EnumType.上报埋点.十五日天气模块左右滑动)
                }
            }
        })

    }

    private fun changeFifChartState(isChart: Boolean) {
        if (isChart && binding.fifWeather.visibility == View.VISIBLE) return
        if (!isChart && binding.frameListFif.visibility == View.VISIBLE) return

        CacheUtil.put(Constant.SP_FIF_IS_CHART, isChart)
        binding.fifWeather.isVisible(isChart)
        binding.frameListFif.isVisible(!isChart)

        if (isChart) {
            binding.chartType.setBackgroundResource(R.drawable.shape_blue_sbg)
            binding.lineType.background = null
            binding.lineType.setTextColor((Color.parseColor("#999999")))
            binding.chartType.setTextColor((Color.parseColor("#379BFF")))
        } else {
            binding.lineType.setBackgroundResource(R.drawable.shape_blue_sbg)
            binding.chartType.background = null
            binding.lineType.setTextColor((Color.parseColor("#379BFF")))
            binding.chartType.setTextColor((Color.parseColor("#999999")))
        }
    }

    fun setWeatherDate(it:WeatherBean){
        weatherData=it
        binding.fifWeather.notifyData(it.nN().ybds.nN())
        if (it.nN().ybds.nN().size >= 7 && !isFifLoadMore) {
            binding.fifListWeather.notifyData(it.nN().ybds.nN().subList(0, 7))
        } else {
            binding.fifListWeather.notifyData(it.nN().ybds.nN())
        }

    }
}