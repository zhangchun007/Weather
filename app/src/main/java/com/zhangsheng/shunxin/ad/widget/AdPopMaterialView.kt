package com.zhangsheng.shunxin.ad.widget

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.zhangsheng.shunxin.R
import com.xinmeng.shadow.mediation.display.BaseMaterialView
import com.xinmeng.shadow.mediation.display.MaterialViewSpec
import com.xinmeng.shadow.mediation.display.api.IMediaView
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.layout_ad_pop.view.*

/*
*
* 广告4  Icon +title+ des
* */
class AdPopMaterialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseMaterialView(context, attrs, defStyleAttr) {
    private var closeview: View? = null
    override fun getLayoutId(): Int {
        return R.layout.layout_ad_pop
    }

    override fun getMediaView(): IMediaView {
        return mediaView
    }

    private val mediaView: IMediaView = object : IMediaView {
        override fun getRoot(): View? {
            val origin: IMediaView? = super@AdPopMaterialView.getMediaView()
            return origin?.root
        }

        override fun showAsStyle(
            i: Int,
            materialViewSpec: MaterialViewSpec?,
            embeddedMaterial: IEmbeddedMaterial?
        ) {
            val origin: IMediaView? = super@AdPopMaterialView.getMediaView()
            origin?.showAsStyle(i, materialViewSpec, embeddedMaterial)
        }

        override fun getLabelView(): ImageView {
            return adv_label_pop
        }
    }



    fun setCloseClick(block: () -> Unit) {
        closeview?.setSingleClickListener {
            block()
        }
    }
    override fun getCloseView(): View? {
        return closeview
    }

    fun setCloseView(view: View) {
        this.closeview = view
    }


    fun setAdInfo(adInfo: IEmbeddedMaterial) {
        try {
            if (adInfo?.materialType == 2 || adInfo?.materialType == 3 || adInfo?.materialType == 4) {
                if (adInfo?.imageList?.isNotEmpty().nN(false)) {
                    val img = adInfo?.imageList?.get(0)
                    val imgUrl = img?.url ?: ""
                    if (TextUtils.isEmpty(imgUrl)) return

                    val blurBG = findViewById<ImageView>(R.id.blur_bg)
                    blurBG.setBackgroundColor(Color.parseColor("#1a000000"))

                    val blurView = findViewById<ImageView>(R.id.blurView)
                    //设置图片圆角角度
                    Glide.with(context).load(imgUrl)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 8)))
                        .into(blurView)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}