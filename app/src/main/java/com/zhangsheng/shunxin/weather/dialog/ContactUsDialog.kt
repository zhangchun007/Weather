
package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.*
import com.my.sdk.core.extra.logutilsimpl.Constant
import com.zhangsheng.shunxin.databinding.WindowContactUsViewBinding
import com.zhangsheng.shunxin.weather.ext.getAppControl

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class ContactUsDialog(context: Activity) :
    BaseDialog(context) {
    override val binding:WindowContactUsViewBinding by inflate()


    override fun initView() {
        binding.title.text= getAppControl().user_need.nN().title
        binding.tvText.text= getAppControl().user_need.nN().contact
        binding.tvCancel.setSingleClickListener {
            dismiss()
        }

        binding.tvCopy.setSingleClickListener {
            putTextIntoClip(binding.tvText.text.toString())
            dismiss()
        }
    }

    override fun getDimAmount(): Float =0.8f

    private fun putTextIntoClip(text: String) {
        val clipboardManager: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("simple text copy", text)
        clipboardManager.setPrimaryClip(clipData)
        xToast("复制成功")
    }


}