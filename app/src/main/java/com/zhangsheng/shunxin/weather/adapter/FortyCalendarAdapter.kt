package com.zhangsheng.shunxin.weather.adapter

import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.necer.utils.CalendarUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.information.bean.FortyCalendarInfo
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import org.joda.time.LocalDate

/**
 * @Description:
 * @Author:         zhangchun
 * @CreateDate:     2021/7/20
 * @Version:        1.0
 */
class FortyCalendarAdapter : RecyclerView.Adapter<FortyCalendarAdapter.FortyCalendarViewHolder>() {

    var mData: MutableList<FortyCalendarInfo> = mutableListOf()
    var itemSelectedListener: onItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FortyCalendarViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_forty_calendar, parent, false)

        return FortyCalendarViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FortyCalendarViewHolder, position: Int) {
        val itemInfo = mData[position]

        if (itemInfo != null) {
            //农历信息
            holder.tvYinLi?.text = if (itemInfo.isToday) "今天" else itemInfo.yinli

            if (itemInfo.isSelect) {
                holder.itemView.setBackgroundResource(R.drawable.forty_calendar_other_bg)
            } else {
                holder.itemView.setBackgroundResource(R.drawable.forty_calendar_other_null_bg)
            }

            //阴历信息
            if (itemInfo.dayOfMonth > 0) {
                var localdata = LocalDate(itemInfo.year, itemInfo.month, itemInfo.dayOfMonth)
                val calendarDate = CalendarUtil.getCalendarDate(localdata)
                //农历部分文字展示优先顺序 替换的文字、农历节日、节气、公历节日、正常农历日期
                var lunarString: String? = ""
                if (!TextUtils.isEmpty(calendarDate.holiday)) {
                    lunarString = calendarDate.holiday
                } else if (!TextUtils.isEmpty(calendarDate.solarTerm)) {
                    lunarString = calendarDate.solarTerm
                } else if (!TextUtils.isEmpty(calendarDate.lunarHoliday)) {
                    lunarString = calendarDate.lunarHoliday
                } else if (!TextUtils.isEmpty(calendarDate.solarHoliday)) {
                    lunarString = calendarDate.solarHoliday
                } else {
                    if (calendarDate.lunar != null) {
                        lunarString =
                            if ("初一" == calendarDate.lunar.lunarOnDrawStr) calendarDate.lunar.lunarMonthStr else calendarDate.lunar.lunarOnDrawStr
                    }
                }
                holder.tvNongLi?.text = lunarString
            } else {
                holder.tvNongLi?.text = ""
            }
            //天气数据
            if (!TextUtils.isEmpty(itemInfo.wtid)) {
                holder.imgWeather?.visibility = View.VISIBLE
                holder.imgWeather?.setImageResource(WeatherUtils.IconBig(itemInfo.wtid))
            } else {
                holder.imgWeather?.visibility = View.INVISIBLE
            }
        }

        holder.itemView.setOnClickListener {
            if (!TextUtils.isEmpty(itemInfo.wtid)) {
                mData[position].isSelect = true
                itemSelectedListener?.onItemSelected(
                    position,
                    mData[position]
                )
            }
        }
    }

    interface onItemClickListener {
        fun onItemSelected(position: Int, bean: FortyCalendarInfo)
    }


    fun setOnItemClickListener(listener: onItemClickListener) {
        itemSelectedListener = listener
    }


    inner class FortyCalendarViewHolder : RecyclerView.ViewHolder {
        var tvYinLi: TextView? = null
        var tvNongLi: TextView? = null
        var imgWeather: ImageView? = null

        constructor(itemView: View) : super(itemView) {
            tvYinLi = itemView.findViewById(R.id.tv_yinli)
            tvNongLi = itemView.findViewById(R.id.tv_nongli)
            imgWeather = itemView.findViewById(R.id.img_weather)
        }
    }

    fun setData(list: List<FortyCalendarInfo>) {
        if (list != null) {
            mData.clear()
            mData.addAll(list)
            notifyDataSetChanged()
        }
    }

}