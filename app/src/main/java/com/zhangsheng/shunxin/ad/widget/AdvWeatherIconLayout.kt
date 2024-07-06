package com.zhangsheng.shunxin.ad.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.maiya.adlibrary.ad.listener.ShowFeedListener
import com.maiya.thirdlibrary.ext.LogE
import com.maiya.thirdlibrary.ext.getActivity
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.ad.AdUtils
import com.zhangsheng.shunxin.databinding.AdvWeatherIconLayoutBinding

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/4/15 10:18
 */
class AdvWeatherIconLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var listener:HeightChangedListener?=null
    val binding: AdvWeatherIconLayoutBinding =
        AdvWeatherIconLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        listener?.onHeightChanged(bottom)
    }

    fun loadAd() {
        binding.desc.loadIconAd(AdConstant.SLOT_BIGFC1)
        binding.icon1.loadIconAd(AdConstant.SLOT_BIGFC2)
        binding.icon.loadIconAd(AdConstant.SLOT_BIGFC3)
    }


    fun setHeightChangedListener(listener: HeightChangedListener){
        this.listener=listener
    }

    interface HeightChangedListener{
        fun onHeightChanged(height:Int)
    }


}