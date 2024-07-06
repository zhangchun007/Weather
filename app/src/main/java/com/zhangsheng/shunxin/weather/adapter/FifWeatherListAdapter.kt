package com.zhangsheng.shunxin.weather.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.activity.FifWeatherActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/5/13 9:43
 */
class FifWeatherListAdapter : SmartRecycleListener() {
    private var data = ArrayList<WeatherBean.YbdsBean>()
    override fun datas(datas: List<Any>) {
        super.datas(datas)
        data.clear()
        data.addAll(datas as List<WeatherBean.YbdsBean>)
    }

    override fun AutoAdapter(
        holder: SmartViewHolder,
        item: Any,
        position: Int
    ) {
        super.AutoAdapter(holder, item, position)
        var bean = (item as WeatherBean.YbdsBean)
        holder.setTextViewText(
            R.id.date, DataUtil.timeStamp2Date(DataUtil.date2Long(bean.fct, "yyyy-MM-dd"), "MM-dd")
                .replace("-", "/")
        )
            .setTextViewText(
                R.id.week,
                DataUtil.isToday(DataUtil.date2Long(bean.fct, "yyyy-MM-dd"))
            )
            .setTextViewText(R.id.tv_weather, bean.wtq)
            .setTextViewText(R.id.temp, "${bean.tcd}°/${bean.tcn}°")

        var icon = holder.findView<ImageView>(R.id.img_weather)
        icon.setImageResource(WeatherUtils.IconBig(bean.wtqid))

        var root = holder.findView<LinearLayout>(R.id.root)
        if (position == 0) {
            root.alpha = 0.5f
        } else {
            root.alpha = 1f
        }

        val tvWeek = holder.findView<TextView>(R.id.week)
        val tvData = holder.findView<TextView>(R.id.date)

        if (position == 1) {
            tvData.setTextColor(AppContext.getContext().resources.getColor(R.color.color_15_333))
            tvWeek.setTextColor(AppContext.getContext().resources.getColor(R.color.color_15_333))
            root.setBackgroundResource(R.drawable.shape8r_f6_bg)
        } else {
            tvData.setTextColor(AppContext.getContext().resources.getColor(R.color.color_15_date))
            tvWeek.setTextColor(AppContext.getContext().resources.getColor(R.color.color_15_week))
            root.setBackgroundResource(android.R.color.transparent)
        }

        root.setOnClickListener {
            skipActivity(FifWeatherActivity::class.java) {
                putExtra("position", position)
                putExtra("source", "15homeyb-15xiagnqin")
            }
        }

    }
}