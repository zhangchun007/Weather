package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.format.DateUtils
import android.text.style.ForegroundColorSpan
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.base.BasePriorityDialog
import com.maiya.thirdlibrary.ext.LogE
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xm.xmcommon.XMCommonManager
import com.zhangsheng.shunxin.databinding.WindowHintViewBinding
import com.zhangsheng.shunxin.databinding.WindowStorageBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.showReport
import kotlinx.android.synthetic.main.window_storage.view.*

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class AppStorageDialog(context: Activity,val isControl:Boolean=true, var block: (Boolean) -> Unit) :
    BasePriorityDialog(context) {
    override val binding: WindowStorageBinding by inflate()

    override fun initView() {

        val text = "便于缓存动态天气背景和语音播报等数据，节省流量。"
        val ss = SpannableString(text)
        val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#FF088EFF"))
        val start = text.indexOfFirst { it.toString() == "，" }
        ss.setSpan(foregroundColorSpan, start + 1, start + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvContent.text = ss

        binding.tvAgree.setSingleClickListener {
            dismiss()
            clickReport(EnumType.上报埋点.app内存储申请弹窗去开启)
            block.invoke(true)
        }

        binding.tvUnAgree.setSingleClickListener {
            dismiss()
            block.invoke(false)
            clickReport(EnumType.上报埋点.app内存储申请弹窗放弃服务)
        }

    }

    override fun getDimAmount(): Float = 0.8f
    override val dialogLevels: Int = EnumType.弹窗优先级.存储挽留

    override fun dialogPresent() {
        super.dialogPresent()
        if (isControl){
            CacheUtil.put(Constant.SP_STROGE_WINDOW_SHOW_TIME, System.currentTimeMillis())
            CacheUtil.getInt(Constant.SP_STROGE_WINDOW_SHOW_NUMBERS, 0).apply {
                CacheUtil.put(Constant.SP_STROGE_WINDOW_SHOW_NUMBERS, this + 1)
            }
        }
        showReport(EnumType.上报埋点.app内存储申请弹窗显示)
    }

}