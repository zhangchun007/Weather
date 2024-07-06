package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.content.Context
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xm.xmcommon.XMCommonManager
import com.zhangsheng.shunxin.databinding.WindowHintViewBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.showReport

/**
 * @Description:新注册抱歉协议页
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class AppHintDialog(context: Activity, private val func: (Boolean) -> Unit) :
    BaseDialog(context) {
    override val binding: WindowHintViewBinding by inflate()


    override fun initView() {
        binding.tvCancel.ClickReport(EnumType.上报埋点.拒绝协议抱歉弹窗再想想) {
            dismiss()
            func(false)
        }

        binding.tvAgree.ClickReport(EnumType.上报埋点.拒绝协议抱歉弹窗同意并进入) {
            CacheUtil.put(Constant.SP_AGREE_PRIVACY, true)
            dismiss()
            func(true)
        }


    }

    override fun getDimAmount(): Float = 0.5f

    override fun show() {
        super.show()
        showReport(EnumType.上报埋点.拒绝协议抱歉弹窗)
    }

}