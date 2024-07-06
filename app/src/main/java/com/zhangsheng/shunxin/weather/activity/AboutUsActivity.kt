package com.zhangsheng.shunxin.weather.activity

import android.os.Bundle
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.meituan.android.walle.WalleChannelReader
import com.zhangsheng.shunxin.BuildConfig
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityAboutUsBinding
import com.zhangsheng.shunxin.weather.common.CommonUrlConstant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.jumpWeb
import com.zhangsheng.shunxin.weather.ext.skipActivity
import org.koin.android.ext.android.inject


class AboutUsActivity : AacActivity<BaseViewModel>() {

    override val vm by inject<BaseViewModel>()
    override val binding by inflate<ActivityAboutUsBinding>()


    override fun initView(savedInstanceState: Bundle?) {

        binding.title.initTitle("关于我们")

        binding.tvVersionName.text = "V${BuildConfig.VERSION_NAME}"
        binding.tvName.text = resources.getText(R.string.app_name)

        binding.tv1.setSingleClickListener {
            jumpWeb(
                CommonUrlConstant.URL_USER_AGREE,
                "服务协议",
                EnumType.上报埋点.用户协议
            )
        }

        binding.tv2.setSingleClickListener {
            jumpWeb(
                CommonUrlConstant.URL_USER_POLICY,
                "隐私政策",
                EnumType.上报埋点.隐私政策
            )
        }

        var index = 0
        binding.im.setSingleClickListener {
            if (index == 5) {
                skipActivity(BebugActivity::class.java)
            } else {
                index++
            }
        }
    }


}