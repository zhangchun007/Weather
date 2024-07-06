package com.zhangsheng.shunxin.weather.adapter

import android.content.Context
import android.widget.TextView
import com.maiya.thirdlibrary.adapter.CommonAdapter
import com.maiya.thirdlibrary.adapter.ViewHolder
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.net.bean.ControlBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/4/1 14:30
 */
class CityListAdapter(
    context: Context?,
    datas: MutableList<ControlBean.HotCityBean>?,
    layoutId: Int,
    val clickFunc: (position: Int, code: String, name: String) -> Unit
) : CommonAdapter<ControlBean.HotCityBean>(context, datas, layoutId) {

    override fun convert(holder: ViewHolder?, bean: ControlBean.HotCityBean?, position: Int) {
        if (holder == null || bean == null) return
        var name = holder.getView<TextView>(R.id.name)
        name.text = bean.title
        name.isSelected = WeatherUtils.getDatas().any { it.regioncode.isNotEmpty()&&it.regioncode == bean.code }
        name.setSingleClickListener {
            clickFunc(position, bean.code, bean.title)
        }

    }
}