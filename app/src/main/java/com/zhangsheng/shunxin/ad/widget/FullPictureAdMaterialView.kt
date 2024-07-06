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
* 全屏广告
*
* */
class FullPictureAdMaterialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ExtAdMaterialView(context, attrs, defStyleAttr) {

    override fun getCloseView(): View = findViewById(R.id.close_view)
    override fun getLayoutId(): Int {
        return R.layout.adv_material_view_full_picture_ad
    }

}