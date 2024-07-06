package com.zhangsheng.shunxin.weather.dialog

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.xToast
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xm.xmcommon.XMCommonManager
import com.zhangsheng.shunxin.databinding.DialogSpeakLoadingBinding
import com.zhangsheng.shunxin.databinding.WindowHintViewBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.showReport
import kotlinx.android.synthetic.main.dialog_speak_loading.view.*

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class WeatherSpeakDialog(context: Activity) :
    BaseDialog(context) {
    private var loading:ValueAnimator?=null
    override val binding:DialogSpeakLoadingBinding by inflate()


    override fun initView() {
        loading = ValueAnimator.ofInt(0, (90..95).random())
        loading?.duration = 500
        loading?.addUpdateListener {
            binding.tvProgress.text = "${it.animatedValue}%"
        }
        loading?.start()

    }

    fun  changeSpeak(boolean: Boolean){
        Try {
            if (boolean){
                loading?.cancel()
                binding.tvProgress.text = "100%"
                if (this.isShowing){
                    this.dismiss()
                }
            }else{
                if (this.isShowing){
                    this.dismiss()
                    xToast("语音加载失败 请稍后再试~")
                }
            }
        }
    }


    override fun getDimAmount(): Float =0.8f

}