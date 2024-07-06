package com.zhangsheng.shunxin.weather.adapter

import android.content.Context
import android.widget.TextView
import com.maiya.thirdlibrary.adapter.CommonAdapter
import com.maiya.thirdlibrary.adapter.ViewHolder
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.db.city_db.InnerJoinResult
import com.zhangsheng.shunxin.weather.ext.ellipsis
import com.zhangsheng.shunxin.weather.utils.WeatherUtils

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/4/1 14:30
 */
class CityList2ndAdapter(
    context: Context,
    datas: List<InnerJoinResult>,
    layoutId: Int,
    val floor:String,
    val clickFunc: (position: Int,bean:InnerJoinResult) -> Unit
) : CommonAdapter<InnerJoinResult>(context, datas, layoutId) {

    override fun convert(holder: ViewHolder, bean: InnerJoinResult, position: Int) {
        var name = holder.getView<TextView>(R.id.name)

        if (floor == "3") {
            name.isSelected = WeatherUtils.getDatas().any { it.regioncode == bean.code }
            name.ellipsis(4, "${bean.name_cn}")
        } else {
            name.ellipsis(4, "${bean.district_cn}")
        }
        name.setSingleClickListener {
            clickFunc(position,bean)
        }
    }
}