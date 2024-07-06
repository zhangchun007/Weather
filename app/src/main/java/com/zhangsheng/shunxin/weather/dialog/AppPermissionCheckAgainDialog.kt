package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.inflate
import com.zhangsheng.shunxin.databinding.WindowPermissionCheckAginBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.showReport

/**
 * @Description: 华为手机判断
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class AppPermissionCheckAgainDialog(context: Activity, private val func: (needCheck: Boolean) -> Unit) :
    BaseDialog(context) {
    override val binding:WindowPermissionCheckAginBinding by inflate()

    var html = "便于缓存动态天气背景和语音播报等数据，<span style=\"color:#088EFF\">节省流量</span>。"

    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.tvContent.text = Html.fromHtml(html)
        }

        binding.tvAgree.ClickReport( EnumType.上报埋点.存储权限挽留弹窗去开启) {
            func(true)
            dismiss()
        }

        binding.tvCancel.ClickReport( EnumType.上报埋点.存储权限挽留弹窗放弃服务) {
            func(false)
            dismiss()
        }

    }

    override fun getDimAmount(): Float =0.5f

    override fun show() {
        super.show()
        showReport(EnumType.上报埋点.存储权限挽留弹窗)
    }

}