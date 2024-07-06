package com.zhangsheng.shunxin.weather.adapter

import android.content.Context
import com.maiya.thirdlibrary.adapter.CommonAdapter
import com.maiya.thirdlibrary.adapter.ViewHolder
import com.maiya.thirdlibrary.ext.nN
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.db.city_db.InnerJoinResult
import kotlinx.android.synthetic.main.activity_city_select.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/4/1 14:51
 */
class SearchCityAdapter(
    context: Context,
    datas: MutableList<InnerJoinResult>,
    layoutId: Int,val func:()->String
) :
    CommonAdapter<InnerJoinResult>(context, datas, layoutId) {
    override fun convert(holder: ViewHolder, bean: InnerJoinResult, position: Int) {
        var str = bean.name_cn.nN().add(bean.district_cn.nN()).add(bean.prov_cn.nN()).add("中国")
        holder.setTextHint(R.id.tv_search, str, func())
    }

    private fun String.add(str2: String): String {
        return when {
            this.isEmpty() -> str2
            str2.isEmpty() -> this
            this == str2 -> this
            this != str2 -> "$this-$str2"
            else -> ""
        }
    }
}