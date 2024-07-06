package com.zhangsheng.shunxin.ad.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.maiya.adlibrary.ad.listener.ShowFeedListener
import com.maiya.thirdlibrary.ext.LogD
import com.maiya.thirdlibrary.ext.getActivity
import com.maiya.thirdlibrary.ext.isVisible
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.xinmeng.shadow.mediation.source.LoadMaterialError
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.AdvWeatherLayoutBinding

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/4/13 14:04
 */
class AdvWeatherLayout : LinearLayout {
    val binding: AdvWeatherLayoutBinding =
        AdvWeatherLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ad_styleable)
        val background =
            a.getResourceId(R.styleable.ad_styleable_background, R.drawable.shape_white_bg)
        val textColor =
            a.getColor(R.styleable.ad_styleable_text_color, Color.parseColor("#222222"))
        val dividerColor =
            a.getColor(R.styleable.ad_styleable_divider_color, context.resources.getColor(R.color.color_ad_bg))
        val dividerHigth = a.getDimensionPixelSize(R.styleable.ad_styleable_divider_higth,
            context.resources.getDimension(R.dimen.ad_divider).toInt()
        )
        a.recycle()

        binding.advM.setBackgroundView(background)
        binding.advM.setTextColorView(textColor)

        binding.advB.setBackgroundView(background)
        binding.advB.setTextColorView(textColor)

        binding.dividerB.setBackgroundColor(dividerColor)
        binding.dividerM.setBackgroundColor(dividerColor)

        binding.dividerM.setPadding(0, dividerHigth, 0, 0)
        binding.dividerB.setPadding(0, dividerHigth, 0, 0)
    }

    fun setCanLoadable(boolean: Boolean) {
        binding.advM.setAdLoadable(boolean)
        binding.advB.setAdLoadable(boolean)
    }

    fun loadAd(pageTypeB: String, pageTypeM: String) {
        binding.advM.showLeftFeedAd(getActivity(), pageTypeM, false, object : ShowFeedListener() {
            override fun onError(p0: LoadMaterialError?) {
                super.onError(p0)
                binding.dividerM.isVisible(false)
            }

            override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                binding.dividerM.isVisible(true)
                return super.onLoad(p0)
            }

            override fun adClose() {
                super.adClose()
                binding.dividerM.isVisible(false)
            }
        })

        binding.advB.showFeedAd(getActivity(), pageTypeB, 4f, false, object : ShowFeedListener() {
            override fun onError(p0: LoadMaterialError?) {
                super.onError(p0)
                binding.dividerB.isVisible(false)
            }

            override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                binding.dividerB.isVisible(true)
                return super.onLoad(p0)
            }

            override fun adClose() {
                super.adClose()
                binding.dividerB.isVisible(false)
            }
        })
    }

}