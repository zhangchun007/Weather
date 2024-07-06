package com.zhangsheng.shunxin.weather.activity

import android.graphics.Color
import android.os.Bundle
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityMoreRainFallBinding
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.net.bean.FortyWeatherBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import org.koin.android.ext.android.inject

class MoreRainFallActivity : AacActivity<BaseViewModel>() {

    override val vm by inject<BaseViewModel>()
    override val binding by inflate<ActivityMoreRainFallBinding>()

    override fun initView(savedInstanceState: Bundle?) {
        binding.title.initTitle("浦东新区 ")
        binding.recycleView.setSmartListener(MyAdapter())
    }

    val mData: ArrayList<FortyWeatherBean.YbdsBean> = ArrayList()

    override fun initObserve() {
        super.initObserve()
        getAppModel().fortyWeatherBean.safeObserve(this, {
            if (it.ybds == null || it.ybds?.isEmpty() == true || it.falls == null || it.falls?.isEmpty() == true) {
                binding.linErrorView.isVisible(true)
            } else {
                mData.clear()
                it.falls?.forEachIndexed { i, data ->
                    if (data.rss == "1") {
                        mData.add(it.ybds.listIndex(i))
                    }
                }
                binding.linErrorView.isVisible(false)
                binding.recycleView.notifyData(mData)
            }
        })
    }

    inner class MyAdapter : SmartRecycleListener() {
        override fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int) {
            super.AutoAdapter(holder, item, position)
            val bean = (item as FortyWeatherBean.YbdsBean)
            val week = DataUtil.getWeek(DataUtil.date2Long(bean.fct, "yyyy-MM-dd"))
            holder.setTextViewText(
                R.id.tv_week,
                week
            ).setTextViewText(
                R.id.tv_day,
                DataUtil.timeStamp2Date(DataUtil.date2Long(bean.fct, "yyyy-MM-dd"), "MM/dd")
            ).setImageRes(R.id.im_weather_icon, WeatherUtils.IconBig(bean.wtid, false))
                .setTextViewText(R.id.tv_weather, bean.wt)
                .setTextViewText(R.id.tv_temp, "${bean.tcn}°~${bean.tcd}°")

            if ("周六" == week || "周日" == week) {
                holder.setTextColor(R.id.tv_week, Color.parseColor("#45B2EF"))
            } else {
                holder.setTextColor(R.id.tv_week, Color.parseColor("#AAAAAA"))
            }
        }
    }
}