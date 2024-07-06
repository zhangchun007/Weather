package com.zhangsheng.shunxin.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.maiya.thirdlibrary.ext.LogE
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.utils.DisplayUtil
import com.zhangsheng.shunxin.weather.utils.DataUtil

class AirMapSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var dates: List<Long>? = null
    private var textPaint: Paint? = null
    private var linePaint: Paint? = null
    private var padding = 20f
    private var with: Int = 0
    private var viewHeight: Int = 0
    private var spacing: Int = 0
    private var seekBarPaint: Paint? = null
    private var progress: Int = 0
    private var listener: ProgressListener? = null

    init {
        textPaint = Paint().apply {
            this.color = Color.parseColor("#A0A0A0")
            this.textSize = DisplayUtil.dip2px(12f).toFloat()
            isAntiAlias = true
            isDither = true
            isFilterBitmap = true
        }
        linePaint = Paint().apply {
            this.color = Color.parseColor("#C7CED6")
            this.strokeWidth = DisplayUtil.dip2px(1f).toFloat()
        }

        seekBarPaint = Paint().apply {
            this.color = Color.parseColor("#088EFF")
            this.strokeWidth = DisplayUtil.dip2px(2f).toFloat()
        }


    }

    fun setDate(date: List<Long>) {
        if (date.isNotEmpty()) {
            this.dates = date
        }
        spacing = (with - DisplayUtil.dip2px(padding) * 2) / date.lastIndex
        invalidate()
    }

    fun setOnProgressListener(listener: ProgressListener) {
        this.listener = listener
    }


    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        with = measuredWidth
        viewHeight = measuredHeight
    }

    private fun drawProgress(canvas: Canvas?, index: Int) {
        val x = padding + (index + 1f) * spacing
        val height = DisplayUtil.dip2px(10f).toFloat()
        linePaint?.let { canvas?.drawLine(x, 0f, x, height, it) }
        val text = if (index == dates?.lastIndex) {
            "现在"
        } else DataUtil.Date2TimeCalendarHourCN(dates.listIndex(index))

        textPaint?.let {
            it.color = Color.parseColor(if (index == dates?.lastIndex) "#242C34" else "#A0A0A0")

            val lRect = Rect()
            it.getTextBounds(text, 0, text.length, lRect)
            canvas?.drawText(
                text,
                x - (lRect.width() / 2),
                height + DisplayUtil.dip2px(18f),
                it
            )
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        dates?.let {
            drawProgress(canvas, 0)
            drawProgress(canvas, it.size / 2)
            drawProgress(canvas, it.lastIndex)

        }

        seekBarPaint?.let {
            val x = padding + (progress + 1f) * spacing
            canvas?.drawLine(x, 0f, x, viewHeight.toFloat(), it)
        }
    }

    interface ProgressListener {
        fun onProgress(progress: Int)
    }
}