package com.zhangsheng.shunxin.information.holders

import android.app.Activity
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.maiya.adlibrary.ad.listener.ShowFeedListener
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.xinmeng.shadow.mediation.source.LoadMaterialError
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.information.bean.InfoCommendBean
import com.zhangsheng.shunxin.ad.widget.ExtAdMaterialView

/**
 * @Author:liupengbing
 * @Data: 2020/9/14 11:15
 * @Email:aliupengbing@163.com
 */
class InfoStreamSmallPicAdHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setData(dataBean: InfoCommendBean.DataBean?, activity: Activity?, position: Int) {
        //view
        val adv_material_view = itemView.findViewById<View>(R.id.adv_material_view)
        val divider=itemView.findViewById<View>(R.id.divider)
        val adSlot = dataBean?.adSlot
        (adv_material_view as ExtAdMaterialView).showLeftFeedAd(
            activity,
            adSlot,
            true,
            object : ShowFeedListener() {
                override fun onError(p0: LoadMaterialError?) {
                    divider.isVisible=false
                    super.onError(p0)
                }

                override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                    divider.isVisible=true
                    return super.onLoad(p0)
                }
            },
            5f,
            5f
        )
    }
}