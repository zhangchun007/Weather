package com.zhangsheng.shunxin.ad.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.zhangsheng.shunxin.R

/*
*
* 大图广告:清理大图
*
* */
class CleanPictureAdMaterialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ExtAdMaterialView(context, attrs, defStyleAttr) {

    override fun getCloseView(): View = findViewById(R.id.big_picture_ad_close)
    override fun getLayoutId(): Int {
        return R.layout.adv_material_view_clean_picture_ad
    }

}