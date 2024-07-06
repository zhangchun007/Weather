package com.zhangsheng.shunxin.weather.adapter

import androidx.constraintlayout.widget.ConstraintLayout
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.parseInt
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.weather.widget.weather.TemperatureView

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/5/13 9:43
 */
class HourWeatherAdapter() : SmartRecycleListener() {
    private var data = ArrayList<WeatherBean.YbhsBean>()
    private var maxTemp = 10
    private var minTemp = 0

    override fun datas(datas: List<Any>) {
        super.datas(datas)
        data.clear()
        data.addAll(datas as List<WeatherBean.YbhsBean>)
        maxTemp =
            data.maxBy { it.weatherDetail.nN().tc.parseInt() }.nN().weatherDetail.nN().tc.parseInt()
        minTemp =
            data.minBy { it.weatherDetail.nN().tc.parseInt() }.nN().weatherDetail.nN().tc.parseInt()
    }

    override fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int) {
        super.AutoAdapter(holder, item, position)
        val weather = (item as WeatherBean.YbhsBean).weatherDetail

        holder.setImageRes(
            R.id.weather,
            WeatherUtils.IconBig(weather.nN().wtid, WeatherUtils.timeIsNight(weather.nN().fct))
        )
            .setTextViewText(R.id.tv_weather, weather.nN().wt)
            .setTextViewText(R.id.tv_wind_ori, weather.nN().wdir)
            .setTextViewText(R.id.tv_wind_level, weather.nN().ws)
            .setTextViewText(R.id.temp, "${weather.nN().tc}°")
            .setTextViewText(R.id.time, if (position == 0) "现在" else weather.nN().fct)
        val next =
            if (data.size - 1 >= position + 1) data.listIndex(position + 1).weatherDetail.nN().tc.parseInt() else 888
        val pre =
            if (position - 1 >= 0) data.listIndex(position - 1).weatherDetail.nN().tc.parseInt() else 888

        holder.findView<TemperatureView>(R.id.weather_char).drawChar(
            AppContext.getContext().resources.getColor(R.color._24_lineColor),
            maxTemp,
            minTemp,
            pre,
            weather.nN().tc.parseInt(),
            next,
            position == 0
        )

        holder.setTextColor(
            R.id.time,
            if (position == 0) holder.itemView.context.resources.getColor(R.color.color_24_now)
            else holder.itemView.context.resources.getColor(R.color.color_24_time)
        )

        val bg = holder.findView<ConstraintLayout>(R.id.mainView)
        bg.setBackgroundResource(if (position == 0) R.drawable.shape8r_f6_bg else android.R.color.transparent)
    }
}