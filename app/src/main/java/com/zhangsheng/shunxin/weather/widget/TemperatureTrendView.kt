package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.maiya.thirdlibrary.ext.isVisible
import com.zhangsheng.shunxin.databinding.LayoutTemperatureTrendViewBinding
import com.zhangsheng.shunxin.weather.net.bean.FortyWeatherBean

class TemperatureTrendView : ConstraintLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
    }

    private val binding: LayoutTemperatureTrendViewBinding =
        LayoutTemperatureTrendViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setData(data: FortyWeatherBean) {
        if (data.ybds == null || data.ybds?.isEmpty() == true) return
        val maxTemp = data.ybds?.maxByOrNull { it.tcd }
        val minTemp = data.ybds?.minByOrNull { it.tcd }
        if (maxTemp == null || minTemp == null) return
        binding.lineChartView.setData(data.ybds, maxTemp.tcd, minTemp.tcd)
        if (TextUtils.isEmpty(data.tcTrend?.up)) {
            binding.tvDay.isVisible(false)
            binding.tv.isVisible(false)
        } else {
            binding.tvDay.text = data.tcTrend?.up ?: ""
        }

        if (TextUtils.isEmpty(data.tcFullDesc)) {
            binding.tvDescribe.isVisible(false)
        } else {
            binding.tvDescribe.text = data.tcFullDesc
        }
    }
}