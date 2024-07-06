package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.zhangsheng.shunxin.databinding.DialogWeatherReminderViewBinding

/***
 * 早晚间天气提醒
 */
class AppWeatherReminderDialog(context: Activity) : BaseDialog(context) {

    override val binding: DialogWeatherReminderViewBinding by inflate()

    override fun getDimAmount(): Float = 0.8f

    override fun initView() {
        binding.tvTitle.text = "白天天气预报"
        binding.imClose.setSingleClickListener {
            dismiss()
        }
    }
}