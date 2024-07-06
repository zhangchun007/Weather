package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.os.Build
import android.text.Html
import com.maiya.thirdlibrary.base.BasePriorityDialog
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.zhangsheng.shunxin.databinding.DialogRequestLocationPermissionBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.closeReport
import com.zhangsheng.shunxin.weather.ext.showReport
import kotlinx.android.synthetic.main.dialog_request_location_permission.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/1/13 10:00
 */

class RequestLocationPermissionDialog(context: Activity,val allow: (isAllow: Boolean) -> Unit) :
    BasePriorityDialog(context) {


    override val binding: DialogRequestLocationPermissionBinding by inflate()
    override val dialogLevels: Int=EnumType.弹窗优先级.首次定位权限

    var html = "便于我们根据您的具体位置提供<span style=\"color:#088EFF\">精准天气</span>和<span style=\"color:#088EFF\">短时降雨预报</span>"

    override fun getDimAmount(): Float = 0.8f

    override fun initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.tvContent.text = Html.fromHtml(html)
        }

        binding.tvAgree.ClickReport(EnumType.上报埋点.新用户流程_定位授权弹窗_点同意) {
            allow(true)
            dismiss()
        }

        binding.tvCancel.setSingleClickListener {
            closeReport(EnumType.上报埋点.新用户流程_定位授权弹窗_点放弃)
            this.dismiss()
            allow(false)
        }

    }

    override fun dialogPresent() {
        super.dialogPresent()
        showReport(EnumType.上报埋点.新用户流程_定位授权弹窗_展示)
    }

}

