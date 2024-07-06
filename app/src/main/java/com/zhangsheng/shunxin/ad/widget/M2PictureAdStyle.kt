package com.zhangsheng.shunxin.ad.widget

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maiya.thirdlibrary.ext.nN
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.zhangsheng.shunxin.R
import jp.wasabeef.glide.transformations.BlurTransformation


/*
*
* 左图右文 M1样式
* */
class M2PictureAdStyle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ExtAdMaterialView(context, attrs, defStyleAttr) {

    override fun getLayoutId(): Int {
        return R.layout.adv_material_view_m2_picture_ad
    }

    override fun getCloseView(): View = findViewById(R.id.middle_style_s2_close)

    fun setBackgroundView(bg: Int) {
        findViewById<View>(R.id.rel_bg)?.setBackgroundResource(bg)
    }

    fun setTextColorView(color: Int) {
        findViewById<TextView>(R.id.adv_title_view)?.setTextColor(color)
    }

    override fun setAdInfo(adInfo: IEmbeddedMaterial) {
        super.setAdInfo(adInfo)
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