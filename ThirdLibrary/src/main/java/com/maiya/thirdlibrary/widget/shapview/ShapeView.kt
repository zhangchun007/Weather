package com.maiya.thirdlibrary.widget.shapview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.R
import com.maiya.thirdlibrary.widget.shapview.listener.TimeListener


/**
 * 作者：全荣浩 on 2018/4/12 13:39
 * 邮箱：672114236@qq.com
 */

class ShapeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var config = Config()


    inner class Config {
        var mediumBold = false
        var shape = 0 //   0矩形、1椭圆形、2线、3环形
        var innerRadius = 0f //内环的半径
        var thickness = 0f //环的厚度
        var radius = 0f
        var topLeftRadius = 0f
        var topRightRadius = 0f
        var bottomLeftRadius = 0f
        var bottomRightRadius = 0f

        var startColor = 0
        var endColor = 0
        var centerColor = 0
        var gradientOrientation = 1
        var angle_type = 0
        var centerX = 0f
        var centerY = 0f
        var stroke = 0f
        var strokeColor = 0
        var paddingLeft = 0f
        var paddingRight = 0f
        var paddingTop = 0f
        var paddingBottom = 0f
        var bgColor = 0
        var dashWidth = 0f
        var dashGap = 0f
        var elevations = 0f
        var isRipple = true
        var parameter = 0.2f
        var pressedColor = 0
        var isPress = false
        var isCenter = true
    }

    private var countDownTime: Long = 0
    private var skipTime: Long = 0
    private var timeListener: TimeListener? = null
    private var countDownTimer: CountDownTimer? = null


    var LEFT_DRAWABLE = 0
    var TOP_DRAWABLE = 1
    var RIGHT_DRAWABLE = 2
    var BOTTOM_DRAWABLE = 3

    private lateinit var gd: GradientDrawable


    private var gtDrawable: Drawable

    init {
        initAttrs(attrs)
        this.isClickable = true

        if (config.mediumBold) {
            this.paint.isFakeBoldText = true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && config.elevations != 0f) {
            val scale = context.resources.displayMetrics.density
            this.elevation = config.elevations.toInt() / scale + 0.5f
        }

        gtDrawable = getColorDrawable()


        if (gtDrawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.background = gtDrawable
            } else {
                this.setBackgroundDrawable(gtDrawable)
            }
        }
        this.setPadding(
            config.paddingLeft.toInt(),
            config.paddingTop.toInt(),
            config.paddingRight.toInt(),
            config.paddingBottom.toInt()
        )


    }

    private fun getColorDrawable(): Drawable {
        this.isClickable = true
        this.isFocusable = true
        if (config.isCenter) {
            gravity = Gravity.CENTER
        }
        if (config.isPress || config.pressedColor != 0 || config.isRipple) {
            return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && config.isRipple) {
                if (config.pressedColor == 0) {
                    config.pressedColor = getLightOrDarken(config.bgColor, 0.2f)
                }
                var pressedColorDw = ColorStateList.valueOf(config.pressedColor)
                RippleDrawable(pressedColorDw, getGtDrawable(0, 0f), getShape())
            } else {
                var stateListDrawable = StateListDrawable()
                stateListDrawable.addState(
                    intArrayOf(android.R.attr.state_pressed),
                    getGtDrawable(config.pressedColor, config.parameter)
                )
                stateListDrawable.addState(
                    intArrayOf(android.R.attr.state_focused),
                    getGtDrawable(config.pressedColor, config.parameter * 2)
                )
                stateListDrawable.addState(intArrayOf(), getGtDrawable(0, 0f))
                stateListDrawable
            }

        } else {
            var stateListDrawable = StateListDrawable()
            stateListDrawable.addState(intArrayOf(), getGtDrawable(0, 0f))
            return stateListDrawable
        }


    }

    private fun getGtDrawable(color: Int, parameter: Float): GradientDrawable {
        //配置渐变方向
        var orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.BL_TR
        when (config.gradientOrientation) {
            0 -> orientation = GradientDrawable.Orientation.BL_TR
            1 -> orientation = GradientDrawable.Orientation.BOTTOM_TOP
            2 -> orientation = GradientDrawable.Orientation.BR_TL
            3 -> orientation = GradientDrawable.Orientation.LEFT_RIGHT
            4 -> orientation = GradientDrawable.Orientation.RIGHT_LEFT
            5 -> orientation = GradientDrawable.Orientation.TL_BR
            6 -> orientation = GradientDrawable.Orientation.TOP_BOTTOM
            7 -> orientation = GradientDrawable.Orientation.TR_BL
        }
        //渐变颜色
        val colors = if (config.centerColor != 0) {
            intArrayOf(config.startColor, config.centerColor, config.endColor) //分别为开始颜色，中间夜色，结束颜色
        } else {
            intArrayOf(config.startColor, config.endColor)
        }
        if (config.startColor != 0) {
            gd = GradientDrawable(orientation, colors)
        } else {
            gd = GradientDrawable()
            gd.setColor(config.bgColor)
        }

        //设置drawable 形状
        when (config.shape) {
            0 -> gd.shape = GradientDrawable.RECTANGLE
            1 -> gd.shape = GradientDrawable.OVAL
            2 -> gd.shape = GradientDrawable.LINE
            3 -> gd.shape = GradientDrawable.RING
        }

        gd.shape = config.shape
        //环形
        if (config.shape == 3) {
            gd.useLevel = false

        }

        //设置渐变类型
        when (config.angle_type) {
            0 -> gd.gradientType = GradientDrawable.LINEAR_GRADIENT
            1 -> gd.gradientType = GradientDrawable.RADIAL_GRADIENT
            2 -> gd.gradientType = GradientDrawable.SWEEP_GRADIENT
        }
        if (config.centerX != 0f) {
            gd.setGradientCenter(config.centerX, config.centerY)
        }

        //圆角
        if (config.radius != 0f) {
            gd.cornerRadius = config.radius
        } else if (config.topLeftRadius != 0f || config.topRightRadius != 0f || config.bottomRightRadius != 0f || config.bottomLeftRadius != 0f) {
            gd.cornerRadii = floatArrayOf(
                config.topLeftRadius,
                config.topLeftRadius,
                config.topRightRadius,
                config.topRightRadius,
                config.bottomRightRadius,
                config.bottomRightRadius,
                config.bottomLeftRadius,
                config.bottomLeftRadius
            )
        }

        //边框
        if (config.stroke != 0f) {
            gd.setStroke(
                config.stroke.toInt(),
                if (config.strokeColor != 0) config.strokeColor else Color.TRANSPARENT
            )

            if (config.dashWidth != 0f) {
                gd.setStroke(
                    config.stroke.toInt(),
                    if (config.strokeColor != 0) config.strokeColor else Color.TRANSPARENT,
                    config.dashWidth,
                    config.dashGap
                )
            } else {
                gd.setStroke(
                    config.stroke.toInt(),
                    if (config.strokeColor != 0) config.strokeColor else Color.TRANSPARENT
                )
            }
        }
        if (parameter != 0f) {
            if (color == 0) {
                gd.setColor(getLightOrDarken(config.bgColor, parameter))
            } else {
                gd.setColor(color)
            }
        }

        return gd
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeView)
        // <!--  0矩形（rectangle）、1椭圆形(oval)、2线(line)、3环形(ring)-->
        config.shape = ta.getInt(R.styleable.ShapeView_shape, 0)
        config.innerRadius = ta.getDimension(R.styleable.ShapeView_innerRadius, 0F)
        config.thickness = ta.getDimension(R.styleable.ShapeView_thickness, 0f)
        config.radius = ta.getDimension(R.styleable.ShapeView_radius, 0f)
        config.topLeftRadius = ta.getDimension(R.styleable.ShapeView_topLeftRadius, 0f)
        config.topRightRadius = ta.getDimension(R.styleable.ShapeView_topRightRadius, 0f)
        config.bottomLeftRadius = ta.getDimension(R.styleable.ShapeView_bottomLeftRadius, 0f)
        config.bottomRightRadius = ta.getDimension(R.styleable.ShapeView_bottomRightRadius, 0f)
        config.isPress = ta.getBoolean(R.styleable.ShapeView_IsPress, true)
        config.startColor = ta.getColor(R.styleable.ShapeView_startColor, 0)
        config.endColor = ta.getColor(R.styleable.ShapeView_endColor, 0)
        config.centerColor = ta.getColor(R.styleable.ShapeView_centerColor, 0)
        config.gradientOrientation = ta.getInt(R.styleable.ShapeView_gradientOrientation, 6)
        config.angle_type = ta.getInt(R.styleable.ShapeView_angle_type, 0)
        config.centerX = ta.getFloat(R.styleable.ShapeView_centerX, 0f)
        config.centerY = ta.getFloat(R.styleable.ShapeView_centerY, 0f)
        config.mediumBold = ta.getBoolean(R.styleable.ShapeView_mediumBold, false)
        config.stroke = ta.getDimension(R.styleable.ShapeView_stroke, 0f)
        config.strokeColor = ta.getColor(R.styleable.ShapeView_strokeColor, 0)
        config.paddingLeft = ta.getDimension(R.styleable.ShapeView_paddingLeft, 0f)
        config.paddingRight = ta.getDimension(R.styleable.ShapeView_paddingRight, 0f)
        config.paddingTop = ta.getDimension(R.styleable.ShapeView_paddingTop, 0f)
        config.paddingBottom = ta.getDimension(R.styleable.ShapeView_paddingBottom, 0f)
        config.bgColor = ta.getColor(R.styleable.ShapeView_bgColor, Color.TRANSPARENT)

        config.dashWidth = ta.getDimension(R.styleable.ShapeView_dashWidth, 0f)
        config.dashGap = ta.getDimension(R.styleable.ShapeView_dashGap, 1f)
        config.elevations = ta.getDimension(R.styleable.ShapeView_elevations, 0f)
        config.isRipple = ta.getBoolean(R.styleable.ShapeView_isRipple, true)
        config.parameter = ta.getFloat(R.styleable.ShapeView_parameter, 0.2f)
        config.pressedColor = ta.getColor(R.styleable.ShapeView_pressedColor, 0)
        config.isCenter = ta.getBoolean(R.styleable.ShapeView_textCenter, true)
        ta.recycle()
    }


    private fun getShape(): Drawable {
        val mask = ShapeDrawable(object : RectShape() {
            override fun draw(canvas: Canvas, paint: Paint) {
                val width = this.width
                val height = this.height
                val rectF = RectF(0f, 0f, width, height)
                if (config.radius != 0f) {
                    canvas.drawRoundRect(rectF, config.radius, config.radius, paint)
                } else if (config.topLeftRadius != 0f || config.topRightRadius != 0f || config.bottomRightRadius != 0f || config.bottomLeftRadius != 0f) {
                    val path = Path()
                    path.addRoundRect(
                        rectF,
                        floatArrayOf(
                            config.topLeftRadius,
                            config.topLeftRadius,
                            config.topRightRadius,
                            config.topRightRadius,
                            config.bottomRightRadius,
                            config.bottomRightRadius,
                            config.bottomLeftRadius,
                            config.bottomLeftRadius
                        ),
                        Path.Direction.CW
                    )
                    canvas.drawPath(path, paint)
                } else {
                    canvas.drawRect(rectF, paint)
                }
            }
        })
        return mask
    }

    //单色变暗
    private fun darkenColor(color: Int, parameter: Float): Int {
        return Math.max(color - color * parameter, 0f).toInt()
    }

    //颜色变暗
    private fun drakenColors(color: Int, parameter: Float): Int {
        var red = Color.red(color)
        var green = Color.green(color)
        var blue = Color.blue(color)
        red = darkenColor(red, parameter)
        green = darkenColor(green, parameter)
        blue = darkenColor(blue, parameter)
        val alpha = Color.alpha(color)
        return Color.argb(alpha, red, green, blue)
    }

    //单色变亮
    private fun lightColor(color: Int, parameter: Float): Int {
        return Math.min(color + color * parameter, 255f).toInt()
    }

    //颜色变亮
    private fun lightColors(color: Int, parameter: Float): Int {
        var red = Color.red(color)
        var green = Color.green(color)
        var blue = Color.blue(color)
        red = lightColor(red, parameter)
        green = lightColor(green, parameter)
        blue = lightColor(blue, parameter)
        val alpha = Color.alpha(color)
        return Color.argb(alpha, red, green, blue)
    }

    //判断颜色 变亮或变暗
    private fun isLightOrDarken(color: Int, parameter: Float): Boolean {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return red + red * parameter < 255 && green + green * parameter < 255 && blue + blue * parameter < 255
    }

    //获取变亮或变暗颜色
    private fun getLightOrDarken(color: Int, parameter: Float): Int {
        var parameter = parameter
        parameter = if (parameter < 0) 0f else if (parameter > 1) 1f else parameter
        return if (isLightOrDarken(color, parameter)) {
            lightColors(color, parameter)
        } else {
            drakenColors(color, parameter)
        }
    }


    //设置背景颜色
    fun setColor(color: Int) {
        config.bgColor = color
//        gtDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.background = getColorDrawable()
        } else {
            this.setBackgroundDrawable(getColorDrawable())
        }
    }

    //设置文字变色
    private var builder: SpannableStringBuilder? = null


    //拼接修改颜色值
    fun appendText(tv: String, color: Int = 0, size: Int = 0, click: () -> Unit = {}) {
        var beforeLength = this.text.length
        var tvsize = if (size == 0) px2dp(textSize) else size
        if (builder == null) builder = SpannableStringBuilder("")
        builder!!.append(tv)
        builder!!.setSpan(
            AbsoluteSizeSpan(tvsize, true), beforeLength,
            beforeLength + tv.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        if (click != {}) {
            this.movementMethod = LinkMovementMethod.getInstance()
            builder!!.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    click()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(context, color)
                    ds.isUnderlineText = false
                }

            }, beforeLength, beforeLength + tv.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            if (color != 0) {
                var tvColor = ContextCompat.getColor(context, color)
                builder!!.setSpan(
                    CharacterStyle.wrap(ForegroundColorSpan(tvColor)),
                    beforeLength, beforeLength + tv.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )//设置后面的字体颜色
            }
        }

        this.text = builder
    }

    //清除文字颜色样式
    fun clearTextSpan() {
        this.text = ""
        if (builder != null) {
            builder!!.clear()
            builder = null
        }
    }

    override fun onDraw(canvas: Canvas?) {

        var drawablePadding = compoundDrawablePadding
        if (drawablePadding != 0) {
            var drawables = compoundDrawables
            drawables?.let {
                var textwidth = paint.measureText(text.toString())
                var textHeight = paint.descent() - paint.ascent()
                var drawableWidth = 0
                drawables[0]?.let {
                    drawableWidth = it.intrinsicWidth
                    var bodyWidth = textwidth + drawableWidth + drawablePadding
                    setPadding(
                        config.paddingLeft.toInt(),
                        config.paddingTop.toInt(),
                        ((width - bodyWidth) +
                                config.paddingRight).toInt(),
                        config.paddingBottom.toInt()
                    )
                    canvas?.translate((width - bodyWidth) / 2, 0f)
                }

                drawables[2]?.let {
                    drawableWidth = it.intrinsicWidth
                    var bodyWidth = textwidth + drawableWidth + drawablePadding
                    setPadding(
                        config.paddingLeft.toInt(),
                        config.paddingTop.toInt(),
                        ((width - bodyWidth) +
                                config.paddingRight).toInt(),
                        config.paddingBottom.toInt()
                    )
                    canvas?.translate((width - bodyWidth) / 2, 0f)
                }

                drawables[1]?.let {
                    drawableWidth = it.intrinsicHeight
                    var bodyHeditht = textHeight + drawableWidth + drawablePadding
                    setPadding(
                        config.paddingLeft.toInt(),
                        config.paddingTop.toInt(),
                        config.paddingRight.toInt(),
                        (height - bodyHeditht).toInt() + config.paddingBottom.toInt()
                    )
                    canvas?.translate(0f, ((height - bodyHeditht) / 2))
                }

                drawables[3]?.let {
                    drawableWidth = it.intrinsicHeight
                    var bodyHeditht = textHeight + drawableWidth + drawablePadding
                    setPadding(
                        config.paddingLeft.toInt(),
                        config.paddingTop.toInt(),
                        config.paddingRight.toInt(),
                        (height - bodyHeditht).toInt() + config.paddingBottom.toInt()
                    )
                    canvas?.translate(0f, ((height - bodyHeditht) / 2))
                }
            }
        }



        super.onDraw(canvas)
    }




    //设置图片
    fun setImageBackGround(
        drawable: Int? = null, position: Int = LEFT_DRAWABLE, drawablePadding: Int? = null
    ) {
        drawablePadding?.let {
            compoundDrawablePadding = dip2px(context, it.toFloat())
        }
        var draw: Drawable? = null
        drawable?.let {
            draw = ContextCompat.getDrawable(context, drawable)
            draw!!.setBounds(0, 0, draw!!.minimumWidth, draw!!.minimumHeight)
        }
        when (position) {
            LEFT_DRAWABLE -> setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null)
            TOP_DRAWABLE -> setCompoundDrawablesWithIntrinsicBounds(null, draw, null, null)
            RIGHT_DRAWABLE -> setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null)
            BOTTOM_DRAWABLE -> setCompoundDrawablesWithIntrinsicBounds(null, null, null, draw)
        }
    }

    //清除图片
    fun clearDrawable() {
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        setPadding(0, 0, 0, 0)
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun px2dp(pxValue: Float): Int {
        var scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /***
     * 倒计时功能
     */

    fun addTime(time: Long, skip: Long = 1000) {
        countDownTime = time
        countDownTime += 1000
        skipTime = skip

        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
        countDownTimer = object : CountDownTimer(countDownTime, 1000) {
            override fun onFinish() {
                Try {
                    timeListener?.onTimeChanged(0)
                    stopTimeCountDown()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                countDownTime -= skip
                Try {
                    if (countDownTime >= 0) {
                        timeListener?.onTimeChanged(countDownTime)
                    } else {
                        timeListener?.onTimeChanged(0)
                        stopTimeCountDown()
                    }
                }
            }
        }
    }

    fun startCountdownTime(timeListener: TimeListener) {
        this.timeListener = timeListener
        if (countDownTimer != null) {
            countDownTimer!!.start()
        }
    }

    fun stopTimeCountDown() {
        if (timeListener != null) {
            timeListener = null
        }
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }

    }

    //获取配置参数
    fun getConfig(): Config {
        var c = config
        return c
    }

    //执行配置参数
    fun exeConfig(c: Config) {
        config = c
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.background = getColorDrawable()
        } else {
            this.setBackgroundDrawable(getColorDrawable())
        }
    }

}