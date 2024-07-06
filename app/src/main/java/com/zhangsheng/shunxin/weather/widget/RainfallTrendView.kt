package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.maiya.thirdlibrary.ext.isVisible
import com.zhangsheng.shunxin.databinding.LayoutRainfallRrendViewBinding
import com.zhangsheng.shunxin.weather.activity.MoreRainFallActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.net.bean.FortyWeatherBean

class RainfallTrendView : ConstraintLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        binding.tvTainMore.ClickReport(EnumType.上报埋点.降水日期点击) {
            skipActivity(MoreRainFallActivity::class.java)
        }
    }

    private val binding: LayoutRainfallRrendViewBinding =
        LayoutRainfallRrendViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setData(data: FortyWeatherBean) {
        if (data.falls == null || data.falls?.isEmpty() == true) return
        binding.rulerView.setData(data.falls)
        if (TextUtils.isEmpty(data.fallTrend)) {
            binding.tvDay.isVisible(false)
            binding.tvDescribe.isVisible(false)
            binding.tv.isVisible(false)
            return
        }
        val day = data.fallTrend.split(" ")
        if (day.size < 2) {
            return
        }
        binding.tvDay.text = day[0]
        binding.tv.text = day[1]
        binding.tvDescribe.text = "预计未来40天将出现${data.fallTrend}".replace(" ", "")
    }
}