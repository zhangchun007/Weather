package com.zhangsheng.shunxin.ad.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.maiya.thirdlibrary.ext.LogE
import com.maiya.thirdlibrary.ext.isVisible
import com.zhangsheng.shunxin.R
import com.xinmeng.shadow.mediation.display.MaterialViewSpec
import com.xinmeng.shadow.mediation.display.MediaView
import com.xinmeng.shadow.mediation.display.api.IMediaView
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.xinmeng.shadow.mediation.view.RoundImageView

/*
*
* 广告4  Icon +title+ des
* */
class S1AdvIconMaterialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ExtAdMaterialView(context, attrs, defStyleAttr) {

    override fun getLayoutId(): Int {
        return R.layout.adv_material_view_adv_icon
    }


    override fun setAdInfo(adInfo: IEmbeddedMaterial) {
        super.setAdInfo(adInfo)
        findViewById<RoundImageView>(R.id.adv_icon_view).isVisible(!adInfo.iconUrl.isNullOrEmpty())
        findViewById<MediaView>(R.id.adv_media_view).isVisible(adInfo.iconUrl.isNullOrEmpty())
    }

    override fun getCloseView(): View =findViewById(R.id.adv_small_pic_close)

    override fun getMediaView(): IMediaView {
        return mediaView
    }

    private val mediaView: IMediaView = object : IMediaView {
        override fun getRoot(): View? {
            return findViewById(R.id.adv_media_view)
        }

        override fun showAsStyle(
            i: Int,
            materialViewSpec: MaterialViewSpec?,
            embeddedMaterial: IEmbeddedMaterial?
        ) {
            val origin: IMediaView? = super@S1AdvIconMaterialView.getMediaView()
            origin?.showAsStyle(i, materialViewSpec, embeddedMaterial)
        }

        override fun getLabelView(): ImageView {
            return findViewById(R.id.adv_label_s3)
        }
    }
}