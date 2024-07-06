package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.view.View
import com.maiya.thirdlibrary.base.BasePriorityDialog
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xinmeng.shadow.base.ShadowConstants
import com.xinmeng.shadow.mediation.display.MaterialViewSpec
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.zhangsheng.shunxin.databinding.WindowAdPopBinding
import com.zhangsheng.shunxin.ad.AdUtils
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.net.bean.AdPopBean
import kotlinx.android.synthetic.main.window_ad_pop.view.*

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class AdPopDialog(context: Activity, val material: IEmbeddedMaterial) :
    BasePriorityDialog(context) {
    override val binding: WindowAdPopBinding by inflate()
    override val dialogLevels: Int = EnumType.弹窗优先级.广告弹窗

    override fun initView() {
        val materialViewSpec = MaterialViewSpec()
        materialViewSpec.context = context
        materialViewSpec.displayOrder =
            intArrayOf(
                ShadowConstants.SHOW_STYLE_TEMPLATE_VIDEO,
                ShadowConstants.SHOW_STYLE_LARGE
            )
        materialViewSpec.dialog = this
        materialViewSpec.scaleType = MaterialViewSpec.SCALE_TYPE_FIX_CENTER
        materialViewSpec.radiusDpArray = floatArrayOf(8f, 8f, 0f, 0f)


        material.render(binding.adPop, materialViewSpec, null)
        binding.adPop.setAdInfo(material)
        binding.adPop.setCloseView(binding.close)
        binding.adPop.setCloseClick{
            dismiss()
        }
    }

    override fun getDimAmount(): Float = 0.8f


    override fun dialogPresent() {
        super.dialogPresent()
        adShow()
    }


    fun adShow() {
        AdUtils.popAdShouldLoad = false
        val pop = CacheUtil.getObj(Constant.SP_AD_POP, AdPopBean::class.java).nN()
        CacheUtil.putObj(Constant.SP_AD_POP, pop.apply {
            this.showMainPopShowTime = System.currentTimeMillis()
            this.showMainPopTimes = this.nN().showMainPopTimes + 1
        })
    }

}