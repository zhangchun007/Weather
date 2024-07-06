package com.maiya.thirdlibrary.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import java.net.URLDecoder
import kotlin.math.roundToInt


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/3/13 20:47
 */
object PicUtils {

    private const val BITMAP_SCALE = 0.2f
    fun tintColor(imageView: ImageView, color: String) {

        var up = imageView.drawable
        if (up != null) {
            val drawableUp: Drawable = DrawableCompat.wrap(up)
            DrawableCompat.setTint(drawableUp, Color.parseColor(color))
            imageView.setImageDrawable(drawableUp)
        }
    }

    fun tintColor(view: View, color: String) {

        var up = view.background
        if (up != null) {
            val drawableUp: Drawable = DrawableCompat.wrap(up)
            DrawableCompat.setTint(drawableUp, Color.parseColor(color))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.background = drawableUp
            } else {
                view.setBackgroundDrawable(drawableUp)
            }
        }
    }

    fun base64ToBitmap(base64Data: String?): Bitmap? {
        val bytes: ByteArray = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun convertViewToBitmap(view: View): Bitmap? {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            ),
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache(false)
        return view.drawingCache
    }


    fun loadImage(context: Context, url: String,func:(drawable:Drawable?)->Unit) {
        Glide.with(context)
            .load(url)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    func(null)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    func(resource)
                    return false
                }
            }).preload()
    }
    fun setViewUrlImage(view: View, url: String, failed: () -> Unit = {}) {
        Try {
            Glide.with(view.context)
                .asDrawable()
                .load(url)
                .into(object : CustomTarget<Drawable>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        failed()
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        Try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                view.background = resource
                            } else {
                                view.setBackgroundDrawable(resource)
                            }
                        }
                    }
                })
        }
    }

    fun setViewUrlres(view: View, res: Int, failed: () -> Unit = {}) {
        Try {
            Glide.with(view.context)
                .asDrawable()
                .load(res)
                .into(object : CustomTarget<Drawable>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        failed()
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        Try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                view.background = resource
                            } else {
                                view.setBackgroundDrawable(resource)
                            }
                        }
                    }
                })
        }
    }

    fun loadTypeUrl(context: Context, view: ImageView, url: String) {
        var url=URLDecoder.decode(url,"UTF-8")
        var imgs = url.split(".")

        if (imgs[imgs.size - 1] == "gif") {
            Glide.with(context).asGif()
                .load(url)
                .into(view)
        } else {
            Glide.with(context)
                .load(url)
                .into(view)
        }
    }

    fun blurImageView(context: Context, img: ImageView, level: Float, color: Int) { // 将图片处理成模糊
        val bitmap: Bitmap? = blurBitmap(context, drawableToBitmap(img.drawable), level)
        if (bitmap != null) {
            val drawable: Drawable = coverColor(context, bitmap, color)
            img.scaleType = ImageView.ScaleType.FIT_XY
            img.setImageDrawable(drawable)
        } else {
            img.setImageBitmap(null)
            img.setBackgroundColor(color)
        }
    }

    /**
     * 将bitmap转成蒙上颜色的Drawable
     *
     * @param context
     * @param bitmap
     * @param color   要蒙上的颜色
     * @return Drawable
     */
    fun coverColor(context: Context, bitmap: Bitmap, color: Int): Drawable {
        val paint = Paint()
        paint.color = color
        val rect =
            RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        Canvas(bitmap).drawRoundRect(rect, 0f, 0f, paint)
        return BitmapDrawable(context.resources, bitmap)
    }

    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param bitmap  需要模糊的图片
     * @return 模糊处理后的图片
     */
    fun blurBitmap(
        context: Context?,
        bitmap: Bitmap?,
        blurRadius: Float
    ): Bitmap? {
        var blurRadius = blurRadius
        if (blurRadius < 0) {
            blurRadius = 0f
        }
        if (blurRadius > 25) {
            blurRadius = 25f
        }
        var outputBitmap: Bitmap? = null
        return try {
            Class.forName("android.renderscript.ScriptIntrinsicBlur")
            // 计算图片缩小后的长宽
            val width = (bitmap.nN().width * BITMAP_SCALE).roundToInt().toInt()
            val height = (bitmap.nN().height * BITMAP_SCALE).roundToInt().toInt()
            if (width < 2 || height < 2) {
                return null
            }
            // 将缩小后的图片做为预渲染的图片。
            val inputBitmap =
                Bitmap.createScaledBitmap(bitmap.nN(), width, height, false)
            // 创建一张渲染后的输出图片。
            outputBitmap = Bitmap.createBitmap(inputBitmap)
            // 创建RenderScript内核对象
            val rs =
                RenderScript.create(context)
            // 创建一个模糊效果的RenderScript的工具对象
            val blurScript =
                    ScriptIntrinsicBlur.create(
                        rs,
                        Element.U8_4(rs)
                    )

            // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
            // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
            val tmpIn =
                Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut =
                Allocation.createFromBitmap(rs, outputBitmap)
            // 设置渲染的模糊程度, 25f是最大模糊度
            blurScript.setRadius(blurRadius)
            // 设置blurScript对象的输入内存
            blurScript.setInput(tmpIn)
            // 将输出数据保存到输出内存中
            blurScript.forEach(tmpOut)
            // 将数据填充到Allocation中
            tmpOut.copyTo(outputBitmap)
            outputBitmap
        } catch (e: Exception) {
            Log.e("Bemboy_Error", "Android版本过低")
            null
        }
    }

    /**
     * 将Drawable对象转化为Bitmap对象
     *
     * @param drawable Drawable对象
     * @return 对应的Bitmap对象
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        //如果本身就是BitmapDrawable类型 直接转换即可
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        //取得Drawable固有宽高
        val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) { //创建一个1x1像素的单位色图
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else { //直接设置一下宽高和ARGB
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        //重新绘制Bitmap
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }



}