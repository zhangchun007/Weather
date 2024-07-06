package com.zhangsheng.shunxin.weather.widget.chart

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/6/22 11:03
 */

class RainChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private lateinit var chartLinePaint: Paint
    private lateinit var chartPaint: Paint
    private lateinit var rainPaint: Paint
    private lateinit var textPaint: Paint
    private var paddingTop = dip2px(13f)
    private var lrPadding = dip2px(10f)
    private var paddingBottom = dip2px(6f)
    private lateinit var bgDrawable: GradientDrawable
    private var distanceY = 0f
    private var animRate=1f
    private lateinit var shadowPaint:Paint
    private var datas=ArrayList<Float>()
    private var pointDistance = 0f

    init {
        initPain()
    }

    private fun initPain() {
        this.background = bgDrawable()

        chartLinePaint = Paint()
        chartLinePaint.color = Color.parseColor("#33FFFFFF")
        chartLinePaint.strokeWidth = dip2px(1f)

        chartPaint = Paint()
        chartPaint.strokeJoin = Paint.Join.ROUND
        chartPaint.strokeWidth = dip2px(1f)
        chartPaint.isAntiAlias = true
        chartPaint.isDither=true   //防抖动
        chartPaint.style = Paint.Style.STROKE
        chartPaint.color = Color.parseColor("#FFFFFF")

        rainPaint = Paint()
        
        textPaint = Paint()
        textPaint.color = Color.parseColor("#FFFFFF")
        textPaint.textSize = dip2px(12f)

        shadowPaint=Paint()
        shadowPaint.style=Paint.Style.FILL
        shadowPaint.strokeWidth=dip2px(4f)
        shadowPaint.color=Color.parseColor("#26FFFFFF")
        shadowPaint.isAntiAlias = true
        shadowPaint.isDither=true   //防抖动


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var textsize = dip2px(10f) + textPaint.measureText("现在")
        distanceY = (measuredHeight - paddingTop - textsize) / 3
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        drawChartBaseLine(canvas)

        drawChartText(canvas)

        drawChart(canvas)
    }

    private fun drawChart(canvas: Canvas) {
        if (datas.isEmpty()) return
        var size= (datas.size-1)
        pointDistance = (width - lrPadding * 2) / if (size<=0) 1 else size

//        shadowPaint.shader = LinearGradient(lrPadding,paddingTop,width.toFloat()-lrPadding,3*distanceY+paddingTop, intArrayOf(Color.parseColor("#000000"),Color.parseColor("#000000")), null, Shader.TileMode.CLAMP)

        var pointX = lrPadding
        var pointY=0f
        var path = Path()
        datas.forEachIndexed { index, d ->
            pointY=getPointY(d.toFloat())
            if (index == 0) {
                pointX = lrPadding
                path.moveTo(pointX,pointY)
            } else {
                pointX += pointDistance
                path.lineTo(pointX,pointY)
            }
        }
        canvas.drawPath(path, chartPaint)

        path.lineTo(pointX,3*distanceY+paddingTop)
        path.lineTo(lrPadding,3*distanceY+paddingTop)
        canvas.drawPath(path,shadowPaint)

    }

    private fun getPointY(d: Float): Float {

        return (distanceY*3+paddingTop)- ((distanceY*3+paddingTop)-when {
            d < 0.08f -> {//无雨
                distanceY * 3f + paddingTop
            }
            d >= 51.30f -> {//暴雨
                paddingTop
            }
            d >= 0.08f && d < 3.44f -> {//小雨
                distanceY * 3f + paddingTop - ((d / (3.44f - 0.08f)) * distanceY)
            }
            d >= 3.44f && d < 11.33f -> {//中雨
                distanceY * 2f + paddingTop - ((d-3.44f) / (11.33f - 3.44f)) * distanceY
            }
            d >= 11.33f && d < 51.30f -> {//大雨
                distanceY + paddingTop - ((d-11.33f) / (51.30f - 11.33f)) * distanceY
            }
            else -> 0f
        })*animRate

    }


    fun setData(datas:List<Float>){
        this.datas.clear()
        this.datas.addAll(datas)
    }


    fun refreshData(datas:List<Float>){
        this.datas.clear()
        this.datas.addAll(datas)
        invalidate()
    }

    fun animShow(){
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 500
        animator.addUpdateListener { animation ->
            if ( animation.animatedValue as Float !=0f){
                animRate = animation.animatedValue as Float
            }
            this.alpha=animRate
            invalidate()
        }

        animator.start()
    }


    fun animDissMiss(){
        val animator = ValueAnimator.ofFloat(1f, 0f)
        animator.duration = 500
        animator.addUpdateListener { animation ->
            if ( animation.animatedValue as Float !=0f){
                animRate = animation.animatedValue as Float
            }
            this.alpha=animRate
            invalidate()
        }

        animator.start()
    }

    private fun drawChartText(canvas: Canvas) {
        var textY = height - paddingBottom - (getTextHeight(textPaint) / 2)
        var testDistanceX =
            (width - (lrPadding * 2) - textPaint.measureText("现在") - textPaint.measureText("1小时后") * 2) / 2
        var textX = lrPadding
        canvas.drawText("现在", textX, textY, textPaint)
        textX += testDistanceX + textPaint.measureText("现在")
        canvas.drawText("1小时后", textX, textY, textPaint)
        textX += testDistanceX + textPaint.measureText("1小时后")
        canvas.drawText("2小时后", textX, textY, textPaint)
    }

    private fun getTextHeight(paint: Paint): Int {
        val forFontMetrics = paint.fontMetrics
        return (forFontMetrics.descent - forFontMetrics.ascent).toInt()
    }

    private fun drawChartBaseLine(canvas: Canvas?) {
        canvas?.let { canvas ->
            (0..3).forEach {
                canvas.drawLine(lrPadding, distanceY * it + paddingTop, width - lrPadding, distanceY * it + paddingTop, chartLinePaint)
            }
        }

    }


    private fun bgDrawable(): GradientDrawable {
        bgDrawable = GradientDrawable()
        bgDrawable.setColor(Color.parseColor("#1AFFFFFF"))
        bgDrawable.cornerRadius = dip2px(4f)
        return bgDrawable
    }

    fun dip2px(dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }


}