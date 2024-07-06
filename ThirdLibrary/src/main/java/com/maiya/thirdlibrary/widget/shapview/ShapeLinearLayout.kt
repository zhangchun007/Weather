package com.maiya.thirdlibrary.widget.shapview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.util.AttributeSet
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import com.maiya.thirdlibrary.R


/**
 * 作者：全荣浩 on 2018/4/12 13:39
 * 邮箱：672114236@qq.com
 */

class ShapeLinearLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var config = Config()

    inner class Config {
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
        var isRipple = false
        var parameter = 0.2f
        var pressedColor = 0
        var isPress = false
    }


    private lateinit var gd: GradientDrawable


    private var gtDrawable: Drawable

    init {
        initAttrs(attrs)
        this.isClickable = true

        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && config.elevations != 0f) {
            val scale = context.resources.displayMetrics.density
            this.elevation = config.elevations.toInt() / scale + 0.5f
        }
        gtDrawable = getColorDrawable()
        if (gtDrawable != null) {
            this.setBackgroundDrawable(gtDrawable)
        }
    }

    private fun getColorDrawable(): Drawable {
        this.isClickable = true
        this.isFocusable = true
        if (config.isPress || config.pressedColor != 0 || config.isRipple) {
            return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && config.isRipple) {
                if (config.pressedColor == 0) {
                    config.pressedColor = getLightOrDarken(config.bgColor, 0.2f)
                }
                var pressedColorDw = ColorStateList.valueOf(config.pressedColor)
                RippleDrawable(pressedColorDw, getGtDrawable(0, 0f), getShape())
            } else {
                var stateListDrawable = StateListDrawable()
                stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), getGtDrawable(config.pressedColor, config.parameter))
                stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), getGtDrawable(config.pressedColor, config.parameter * 2))
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
            intArrayOf(config.startColor, config.endColor) //分别为开始颜色，中间夜色，结束颜色
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
            gd.cornerRadii = floatArrayOf(config.topLeftRadius, config.topLeftRadius, config.topRightRadius, config.topRightRadius, config.bottomRightRadius, config.bottomRightRadius, config.bottomLeftRadius, config.bottomLeftRadius)
        }

        //边框
        if (config.stroke != 0f) {
            gd.setStroke(config.stroke.toInt(), if (config.strokeColor != 0) config.strokeColor else Color.TRANSPARENT)

            if (config.dashWidth != 0f) {
                gd.setStroke(config.stroke.toInt(), if (config.strokeColor != 0) config.strokeColor else Color.TRANSPARENT, config.dashWidth, config.dashGap)
            } else {
                gd.setStroke(config.stroke.toInt(), if (config.strokeColor != 0) config.strokeColor else Color.TRANSPARENT)
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
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeLinearLayout)
        // <!--  0矩形（rectangle）、1椭圆形(oval)、2线(line)、3环形(ring)-->
        config.shape = ta.getInt(R.styleable.ShapeLinearLayout_ll_shapes, 0)
        config.innerRadius = ta.getDimension(R.styleable.ShapeLinearLayout_ll_InnerRadius, 0F)
        config.thickness = ta.getDimension(R.styleable.ShapeLinearLayout_ll_Thickness, 0f)
        config.radius = ta.getDimension(R.styleable.ShapeLinearLayout_ll_Radius, 0f)
        config.topLeftRadius = ta.getDimension(R.styleable.ShapeLinearLayout_ll_TopLeftRadius, 0f)
        config.topRightRadius = ta.getDimension(R.styleable.ShapeLinearLayout_ll_TopRightRadius, 0f)
        config.bottomLeftRadius = ta.getDimension(R.styleable.ShapeLinearLayout_ll_BottomLeftRadius, 0f)
        config.bottomRightRadius = ta.getDimension(R.styleable.ShapeLinearLayout_ll_BottomRightRadius, 0f)

        config.startColor = ta.getColor(R.styleable.ShapeLinearLayout_ll_StartColor, 0)
        config.endColor = ta.getColor(R.styleable.ShapeLinearLayout_ll_EndColor, 0)
        config.centerColor = ta.getColor(R.styleable.ShapeLinearLayout_ll_CenterColor, 0)
        config.gradientOrientation = ta.getInt(R.styleable.ShapeLinearLayout_ll_GradientOrientation, 1)
        config.angle_type = ta.getInt(R.styleable.ShapeLinearLayout_ll_Angle_type, 0)
        config.centerX = ta.getFloat(R.styleable.ShapeLinearLayout_ll_CenterX, 0f)
        config.centerY = ta.getFloat(R.styleable.ShapeLinearLayout_ll_CenterY, 0f)

        config.stroke = ta.getDimension(R.styleable.ShapeLinearLayout_ll_Stroke, 0f)
        config.strokeColor = ta.getColor(R.styleable.ShapeLinearLayout_ll_StrokeColor, 0)
        config.paddingLeft = ta.getDimension(R.styleable.ShapeLinearLayout_ll_PaddingLeft, 0f)
        config.paddingRight = ta.getDimension(R.styleable.ShapeLinearLayout_ll_PaddingRight, 0f)
        config.paddingTop = ta.getDimension(R.styleable.ShapeLinearLayout_ll_PaddingTop, 0f)
        config.paddingBottom = ta.getDimension(R.styleable.ShapeLinearLayout_ll_PaddingBottom, 0f)
        config.bgColor = ta.getColor(R.styleable.ShapeLinearLayout_ll_BgColor, Color.WHITE)

        config.dashWidth = ta.getDimension(R.styleable.ShapeLinearLayout_ll_DashWidth, 0f)
        config.dashGap = ta.getDimension(R.styleable.ShapeLinearLayout_ll_DashGap, 1f)
        config.elevations = ta.getDimension(R.styleable.ShapeLinearLayout_ll_Elevations, 0f)
        config.isRipple = ta.getBoolean(R.styleable.ShapeLinearLayout_ll_IsRipple, false)
        config.parameter = ta.getFloat(R.styleable.ShapeLinearLayout_ll_Parameter, 0.2f)
        config.pressedColor = ta.getColor(R.styleable.ShapeLinearLayout_ll_PressedColor, 0)
        config.isPress = ta.getBoolean(R.styleable.ShapeLinearLayout_ll_isPress, false)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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
                    path.addRoundRect(rectF, floatArrayOf(config.topLeftRadius, config.topLeftRadius, config.topRightRadius, config.topRightRadius, config.bottomRightRadius, config.bottomRightRadius, config.bottomLeftRadius, config.bottomLeftRadius), Path.Direction.CW)
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
        this.setBackgroundDrawable(getColorDrawable())
    }

    fun setDrawable(draw: Int) {
        this.setBackgroundResource(draw)
    }

    fun getConfig(): Config {
        var c = config
        return c
    }

    fun exeConfig(c: Config) {
        config = c
        this.setBackgroundDrawable(getColorDrawable())
    }

}