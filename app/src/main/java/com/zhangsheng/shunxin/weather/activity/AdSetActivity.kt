package com.zhangsheng.shunxin.weather.activity

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.utils.CacheUtil
import com.meituan.android.walle.WalleChannelReader
import com.zhangsheng.shunxin.databinding.ActivityAdSetBinding
import com.zhangsheng.shunxin.weather.common.CommonUrlConstant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.jumpWeb
import org.koin.android.ext.android.inject

class AdSetActivity : AacActivity<BaseViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        binding.title.initTitle("广告设置")
        val text = "我们的合作伙伴会通过SDK收集不能识别您个人身份的信息，以此形成您的间接画像标签，并根据您的画像标签向您推荐符合您兴趣的广告，详见《隐私政策》"
        binding.tvContent.movementMethod = LinkMovementMethod.getInstance()
        binding.tvContent.text = generateSp(text)

        val isChoose = CacheUtil.getBoolean("ad_set_key", true)
        binding.cb.isChecked = isChoose

        binding.cb.setOnCheckedChangeListener { view, isChecked ->
            CacheUtil.put("ad_set_key", isChecked)
            if (isChecked) {
                clickReport(EnumType.上报埋点.设置广告设置个性化广告推送开)
            } else {
                clickReport(EnumType.上报埋点.设置广告设置个性化广告推送关)
            }
        }
    }

    override val vm by inject<BaseViewModel>()
    override val binding by inflate<ActivityAdSetBinding>()

    private fun generateSp(text: String): SpannableString {
        val sp = SpannableString(text)
        Try {
            val start = text.indexOfFirst { it.toString() == "《" }
            sp.setSpan(object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = Color.parseColor("#FF088EFF")
                    ds.isUnderlineText = false
                }

                override fun onClick(widget: View) {
                    jumpWeb(
                        CommonUrlConstant.URL_USER_POLICY,
                        "隐私协议",
                        EnumType.上报埋点.隐私政策
                    )
                }
            }, start, start + 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        return sp
    }
}