package com.maiya.thirdlibrary.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.R
import com.maiya.thirdlibrary.databinding.LoadingViewBinding
import com.maiya.thirdlibrary.ext.isStr
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.utils.ActivityManageTools

class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseLoadingView(context, attrs, defStyleAttr) {
    private val binding: LoadingViewBinding =
        LoadingViewBinding.inflate(LayoutInflater.from(context), this, true)
    private var status: Boolean = true

    init {
        binding.imBack.setSingleClickListener {
            if (context is Activity) {
                (context as Activity).finish()
            } else {
                ActivityManageTools.topActivity()?.finish()
            }
        }
    }

    override var getErrorClickView: View? = null


    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        val visible = visibility == View.VISIBLE && getVisibility() == View.VISIBLE
        if (visible && status) {
            statAnimation()
        } else {
            stopAnimation()
        }
    }

    override fun setViewStatus(status: String) {
        when (status) {
            LOADING -> {
                this.status = true
                statAnimation()
                binding.tvHint.text = "努力加载中..."
                binding.tvDoadError.isVisible(false)
            }
            ERROR -> {
                this.status = false
                stopAnimation()
                binding.imLoad.setImageResource(R.mipmap.im_load_error)
                binding.tvHint.text = "网络不给力，请重试"
                binding.tvDoadError.isVisible(true)
            }
        }
    }

    override fun setHintText(text: String) {
        binding.tvHint.text = if (text.isStr()) "网络不给力，请重试" else text
    }

    private fun statAnimation() {
        Try {
            binding.imLoad.setAnimation("loading.json")
            binding.imLoad.progress = 0f
            binding.imLoad.loop(true)
            binding.imLoad.playAnimation()
        }
    }

    private fun stopAnimation() {
        Try {
            binding.imLoad.cancelAnimation()
        }
    }
}