package com.zhangsheng.shunxin.weather.adapter

import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.activity.FifWeatherActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.weather.widget.weather.FifTemperatureView

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/5/13 9:43
 */
class FifWeatherAdapter : SmartRecycleListener() {
    private var data = ArrayList<WeatherBean.YbdsBean>()
    private var dayMaxTemp = 0
    private var dayMinTemp = 0

    private var nightMaxTemp = 0
    private var nightMinTemp = 0
    private var isAirShow = true

    override fun datas(datas: List<Any>) {
        super.datas(datas)
        data.clear()
        data.addAll(datas as List<WeatherBean.YbdsBean>)

        isAirShow = datas.any { it.aqi != "0" && it.aqi != "" }
        dayMaxTemp = data.maxBy { it.nN().tcd }.nN().tcd
        dayMinTemp = data.minBy { it.nN().tcd }.nN().tcd

        nightMaxTemp = data.maxBy { it.nN().tcn }.nN().tcn
        nightMinTemp = data.minBy { it.nN().tcn }.nN().tcn
    }

    override fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int) {
        super.AutoAdapter(holder, item, position)
        var bean = (item as WeatherBean.YbdsBean)

        holder.setTextViewText(
            R.id.tv_week,
            DataUtil.isToday(DataUtil.date2Long(bean.fct, "yyyy-MM-dd"))
        )
            .setTextViewText(
                R.id.tv_date,
                DataUtil.timeStamp2Date(DataUtil.date2Long(bean.fct, "yyyy-MM-dd"), "MM-dd")
                    .replace("-", "/")
            )
            .setImageRes(R.id.iv_day_weather, WeatherUtils.IconBig(bean.wtdid, false))
            .setImageRes(R.id.iv_night_weather, WeatherUtils.IconBig(bean.wtnid, true))
            .setTextViewText(R.id.tv_day_weather, bean.wtd)
            .setTextViewText(R.id.tv_night_weather, bean.wtn)
            .setTextViewText(R.id.tv_day_temp, "${bean.tcd}°")
            .setTextViewText(R.id.tv_night_temp, "${bean.tcn}°")
            .setTextViewText(R.id.tv_wind_ori, bean.wdir)
            .setTextViewText(R.id.tv_wind_level, bean.ws)
            .setTextViewText(R.id.tv_air_level, bean.aqiLevel)
            .setOnItemClickListener {
                clickReport(EnumType.上报埋点.十五日天气模块点击)
                skipActivity(FifWeatherActivity::class.java) {
                    this.putExtra("position", position)
                }
            }
        var air_color = holder.findView<ShapeView>(R.id.air_color)
        WeatherUtils.airColor(bean.aqiLevel.nN(), air_color)
        air_color.isVisible(isAirShow)
        var air_leverl = holder.findView<TextView>(R.id.tv_air_level)
        air_leverl.isVisible(isAirShow)
        var dayNext = if (data.size - 1 >= position + 1) data.listIndex(position + 1).tcd else 888
        var dayPre = if (position - 1 >= 0) data.listIndex(position - 1).tcd else 888
        holder.findView<FifTemperatureView>(R.id.ttv_day)
            .drawChar(holder.itemView.context.getString(R.string.weahter_15_day_line), dayMaxTemp, dayMinTemp, dayPre, bean.nN().tcd, dayNext, position)

        var nightNext = if (data.size - 1 >= position + 1) data.listIndex(position + 1).tcn else 888
        var nightPre = if (position - 1 >= 0) data.listIndex(position - 1).tcn else 888


        holder.findView<FifTemperatureView>(R.id.ttv_night).drawChar(
            holder.itemView.context.getString(R.string.weahter_15_night_line),
            nightMaxTemp,
            nightMinTemp,
            nightPre,
            bean.nN().tcn,
            nightNext,
            position
        )

        var ll_top = holder.findView<LinearLayout>(R.id.ll_top)
        var ll_bottom = holder.findView<LinearLayout>(R.id.ll_bottom)
        if (position == 0) {
            ll_top.alpha = 0.5f
            ll_bottom.alpha = 0.5f
        } else {
            ll_top.alpha = 1f
            ll_bottom.alpha = 1f
        }

        val root = holder.findView<ConstraintLayout>(R.id.root)
        val tvWeek = holder.findView<TextView>(R.id.tv_week)
        if (position == 1) {
            root.setBackgroundResource(R.drawable.shape8r_f6_bg)
            tvWeek.setTextColor(AppContext.getContext().resources.getColor(R.color.color_15_343434))
        } else {
            root.setBackgroundResource(android.R.color.transparent)
            tvWeek.setTextColor(AppContext.getContext().resources.getColor(R.color.color_15_week1))
        }
    }
}