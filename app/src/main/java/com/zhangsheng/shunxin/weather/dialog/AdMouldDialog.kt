package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.content.Context
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DialogPriorityUtil
import com.maiya.thirdlibrary.utils.priority.IPriorityDialog
import com.xinmeng.shadow.mediation.api.IInterstitialListener
import com.xinmeng.shadow.mediation.source.IInterstitialMaterial
import com.zhangsheng.shunxin.ad.AdUtils
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.net.bean.AdPopBean

class AdMouldDialog(val context: Activity, val material: IInterstitialMaterial) : IPriorityDialog {
    protected var isRetryShow = false //优先级第二次展示
    protected var isForceShow = false //强制展示

    private  var isShowing=false
    override fun reShow() {
        isRetryShow = true
        dialogShow()
    }

    override fun dialogPresent() {
    }

    override fun forceShow() {
        isForceShow = true
        showAd()
    }


    override fun isShowing(): Boolean =isShowing

    override fun getDialogLevel(): Int = EnumType.弹窗优先级.广告弹窗

    override fun dialogDismiss() {
        DialogPriorityUtil.dialogDismiss(isForceShow)
    }

    override fun dialogShow() {
        when {
            isRetryShow -> {
                if (this.context == null || this.context.isFinishing || this.context.isDestroyed) return
                showAd()
                dialogPresent()
            }
            DialogPriorityUtil.dialogShow(context, this, getDialogLevel(), isForceShow) -> {
                if (this.context == null || this.context.isFinishing || this.context.isDestroyed) return
                showAd()
                dialogPresent()
            }
        }
    }

    override fun getTargetContext(): Activity =context

    private fun showAd() {
        material.show(context, object : IInterstitialListener {
            override fun onAdShow() {
                isShowing=true
                AdUtils.popAdShouldLoad = false
                val pop =
                    CacheUtil.getObj(Constant.SP_AD_POP, AdPopBean::class.java).nN()
                CacheUtil.putObj(Constant.SP_AD_POP, pop.apply {
                    this.showMainPopShowTime = System.currentTimeMillis()
                    this.showMainPopTimes = this.nN().showMainPopTimes + 1
                })
            }

            override fun onAdClick() {
            }

            override fun onAdClose() {
                isShowing=false
                dialogDismiss()
            }
        })
    }
}