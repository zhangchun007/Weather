package com.zhangsheng.shunxin.information.holders

import android.app.Activity
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.maiya.adlibrary.ad.WindowFocusRelativeLayout
import com.maiya.adlibrary.ad.listener.ShowFeedListener
import com.zhangsheng.shunxin.R
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.xinmeng.shadow.mediation.source.LoadMaterialError
import com.zhangsheng.shunxin.information.bean.InfoBean
import com.zhangsheng.shunxin.ad.widget.ExtAdMaterialView

/**
 * @Author:liupengbing
 * @Data: 2020/9/14 11:15
 * @Email:aliupengbing@163.com
 */
class InfoStreamBigPicAdHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var adBigPic: IEmbeddedMaterial? = null

    fun setData(dataBean: InfoBean.DataBean?, activity: Activity?) {
        //view
        val adv_material_view = itemView.findViewById<View>(R.id.adv_material_view)
        val window=itemView.findViewById<WindowFocusRelativeLayout>(R.id.window_focus)
        val divider=itemView.findViewById<View>(R.id.divider)
        val adSlot =dataBean?.adSlot
        adBigPic?.onResume()
        adBigPic?.resumeVideo()

        window.setWindowFocusChangeListener {
            if (it) {
                (adv_material_view as ExtAdMaterialView).adResume()
            } else {
                (adv_material_view as ExtAdMaterialView).adPause()
            }
        }

        adSlot?.let {
            (adv_material_view as ExtAdMaterialView).showFeedAd(activity,it,4f,true,object :ShowFeedListener(){
                override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                    divider.isVisible=true
                    return super.onLoad(p0)
                }

                override fun onError(p0: LoadMaterialError?) {
                    super.onError(p0)
                    divider.isVisible=false
                }
            })


        }
    }
}