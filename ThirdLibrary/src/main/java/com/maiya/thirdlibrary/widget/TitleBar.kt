package com.maiya.thirdlibrary.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.maiya.thirdlibrary.R
import com.maiya.thirdlibrary.databinding.TitleBarBinding
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.PicUtils
import com.maiya.thirdlibrary.utils.StatusBarUtil
import java.net.DatagramSocket

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/3/24 16:27
 */
open class TitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var binding: TitleBarBinding =
        TitleBarBinding.inflate(LayoutInflater.from(context), this, true)


    fun setIcon(res: Int, color: String = "") {
        binding.titleIcon.setImageResource(res)
        if (color.isNotEmpty()) {
            PicUtils.tintColor(binding.titleIcon, color)
        }
    }

    fun initTitle(title: String = "", themeColor: String = "") {
        binding.llTopbar.isVisible(true)
        if (binding.statusBar.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = binding.statusBar.layoutParams as ViewGroup.MarginLayoutParams
            p.height =
                StatusBarUtil.getStatusBarHeight(AppContext.getContext())
            binding.statusBar.requestLayout()
        }
        binding.back.setOnClickListener {
            this.getActivity()?.finish()
        }
        if (title.isNotEmpty()) {
            binding.tvTitle.text = title
        }

        if (themeColor.isNotEmpty()) {
            binding.tvTitle.setTextColor(Color.parseColor(themeColor))
            PicUtils.tintColor(binding.back, themeColor)
        } else {
            binding.tvTitle.setTextColor(Color.parseColor("#222222"))
            PicUtils.tintColor(binding.back, "#222222")
        }
    }

    fun hideTitle() {
        binding.llTopbar.isVisible(false)
    }

    fun setTitleBgColor(color: String) {
        binding.llTopbar.setBackgroundColor(Color.parseColor(color))
    }


    fun setRightTvStr(content:String){
        binding.btnRight.text=content
    }

    fun setRightTvColor(color:String){
        binding.btnRight.setTextColor(Color.parseColor(color))
    }

    fun setRightTvClick(func:()->Unit){
        binding.btnRight.setSingleClickListener {
            func()
        }
    }

    fun setStatusPading(view: View) {
        view.setPadding(0, StatusBarUtil.getStatusBarHeight(context), 0, 0)
    }

    private fun getActivity(): Activity? {
        if (context is Activity) {
            return context as Activity
        }
        return null
    }


}