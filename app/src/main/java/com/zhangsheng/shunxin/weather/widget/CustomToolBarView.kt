package com.zhangsheng.shunxin.weather.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.utils.ActivityManageTools
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.StatusBarUtil
import com.zhangsheng.shunxin.R
import kotlinx.android.synthetic.main.layout_toolbar_view.view.*

class CustomToolBarView : RelativeLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initView(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout_toolbar_view, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.toolbar_styleable)
        val title = a.getString(R.styleable.toolbar_styleable_title)
        val subtitle = a.getString(R.styleable.toolbar_styleable_subtitle)
        val bgColor = a.getColor(R.styleable.toolbar_styleable_background_color, Color.WHITE)
        a.recycle()

        post {
            if (status_bar.layoutParams is ViewGroup.MarginLayoutParams) {
                val p = status_bar.layoutParams as ViewGroup.MarginLayoutParams
                p.height = StatusBarUtil.getStatusBarHeight(AppContext.getContext())
                status_bar.requestLayout()
            }
        }

        tv_title.text = title
        tv_subtitle.text = subtitle
        mainView.setBackgroundColor(bgColor)

        im_close.setSingleClickListener {
            if (context is Activity) {
                (context as Activity).finish()
            } else {
                ActivityManageTools.topActivity()?.finish()
            }
        }
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }

    fun setSubtitle(title: String) {
        tv_subtitle.text = title
    }

    fun getSubtitle(): TextView {
        return tv_subtitle
    }

    fun setSubTitleClick(block: (View) -> Unit) {
        tv_subtitle.setSingleClickListener(block = block)
    }
}