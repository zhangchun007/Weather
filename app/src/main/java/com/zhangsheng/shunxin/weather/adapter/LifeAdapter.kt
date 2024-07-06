package com.zhangsheng.shunxin.weather.adapter

import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/5/30 15:19
 */
class LifeAdapter(context: Activity) : SmartRecycleListener() {
    override fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int) {
        super.AutoAdapter(holder, item, position)
        var bean = item as WeatherBean.LifesBean
        holder.findView<View>(R.id.divider_ver)
            .isVisible((position + 1) % 3 != 0 || position == 0)

        if (WeatherUtils.lifeIcon(bean.name) != 0) {
            holder.setImageRes(R.id.img, WeatherUtils.lifeIcon(bean.name))
        }
        holder.findView<TextView>(R.id.name).isVisible(true)
        holder.setTextViewText(R.id.state, bean.lv)
            .setTextViewText(R.id.name, bean.name)
            .findView<LinearLayout>(R.id.ll_life)
    }
}