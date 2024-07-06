package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.utils.AppUtils
import com.maiya.thirdlibrary.utils.CacheUtil
import com.meituan.android.walle.WalleChannelReader
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.WindowProtocolBinding
import com.zhangsheng.shunxin.weather.common.CommonUrlConstant
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.jumpWeb
import com.zhangsheng.shunxin.weather.ext.showReport

/**
 * @Description: 新注册用户协议页
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class ProtocolDialog(context: Activity, private val func: (isAgree: Boolean) -> Unit) :
    BaseDialog(context) {
    override val binding:WindowProtocolBinding by inflate()
    override fun initView() {
        initContent()

        binding.tvAgree.ClickReport(EnumType.上报埋点.新用户协议同意并进入){
            CacheUtil.put(Constant.SP_AGREE_PRIVACY, true)
            dismiss()
            func(true)
        }

        binding.tvUnAgree.ClickReport(EnumType.上报埋点.新用户协议不同意){
            dismiss()
            func(false)
        }
    }

    override fun getDimAmount(): Float =0.5f

    override fun show() {
        super.show()
        showReport(EnumType.上报埋点.新用户协议显示)
        XMLogAgent.onPageStart(EnumType.上报埋点.新用户协议弹窗)
    }

    override fun dismiss() {
        super.dismiss()
        XMLogAgent.onPageEnd(EnumType.上报埋点.新用户协议弹窗)
    }

    private fun initContent() {
        binding.tvContent.let { tv_content->
            tv_content.appendText("感谢您使用我们的APP，我们非常重视您的个人信息保护和隐私安全。为了更好地保障您的个人权益，请您充分阅读并理解", R.color.protocol_content_normal)
            tv_content.appendText("《用户服务协议》", R.color.protocol_content_url,16){
                jumpWeb(CommonUrlConstant.URL_USER_AGREE, "服务协议",EnumType.上报埋点.用户协议)
            }
            tv_content.appendText("和", R.color.protocol_content_normal)

            tv_content.appendText("《隐私政策》",  R.color.protocol_content_url,16){
                jumpWeb(CommonUrlConstant.URL_USER_POLICY, "隐私协议",EnumType.上报埋点.隐私政策)
            }
            tv_content.appendText("的全部内容，同意并接受全部条款后开始使用我们的产品和服务。",R.color.protocol_content_normal)
            tv_content.highlightColor= Color.TRANSPARENT
            tv_content.gravity= Gravity.LEFT
        }
    }
}