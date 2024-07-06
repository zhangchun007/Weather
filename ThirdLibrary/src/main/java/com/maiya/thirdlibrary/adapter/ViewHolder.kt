package com.maiya.thirdlibrary.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.maiya.thirdlibrary.ext.nN

class ViewHolder(context: Context, parent: ViewGroup, layoutId: Int, private var mPosition: Int) {
    private var mViews: SparseArray<View> = SparseArray()
    var convertView: View = LayoutInflater.from(context).inflate(layoutId, parent, false)

    init {
        this.convertView.tag = this
    }


    fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews.get(viewId)

        if (null == view) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }

        return view as T
    }


    fun setText(viewId: Int, text: String): ViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text

        return this
    }

    fun setTextHint(viewId: Int, text: String, hintText: String): ViewHolder {
        val tv = getView<TextView>(viewId)

        var fstart = text.indexOf(hintText)
        var fend = fstart + hintText.length
        if (fend != 0 && fstart != -1) {
            var style = SpannableStringBuilder(text)
            style.setSpan(
                ForegroundColorSpan(Color.parseColor("#2BB5FF")), fstart, fend,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            tv.nN().text = style
        } else {
            tv.nN().text = text
        }
        return this
    }

    fun setName(viewId: Int, text: String): ViewHolder {
        var tv = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    fun setGlide(context: Context, viewId: Int, url: Int): ViewHolder {
        var img = getView<ImageView>(viewId)
        img?.let { Glide.with(context).load(url).into(it) }

        return this
    }

    fun setGlide(context: Context, viewId: Int, url: String, default: Int): ViewHolder {
        var img = getView<ImageView>(viewId)
        img?.let {
            Glide.with(context).load(url)
                .error(default).into(it)
        }

        return this
    }


    fun setFragmentGlide(context: Fragment, viewId: Int, url: Int): ViewHolder {
        var img = getView<ImageView>(viewId)
        img?.let { Glide.with(context).load(url).into(it) }

        return this
    }

    fun setTintGlide(context: Context, viewId: Int, url: Int, colorId: Int): ViewHolder {
        var draw = ContextCompat.getDrawable(context, url)
        var drawUp = DrawableCompat.wrap(draw.nN())
        DrawableCompat.setTint(drawUp, ContextCompat.getColor(context, colorId))
        var img = getView<ImageView>(viewId)
        img.setImageDrawable(drawUp)

        return this
    }

    fun setImgeUrl(context: Context, viewId: Int, url: String): ViewHolder {
        var img = getView<ImageView>(viewId)
        img?.let { Glide.with(context).load(url).into(it) }

        return this
    }

    companion object {

        operator fun get(
            context: Context, convertView: View?,
            parent: ViewGroup, layoutId: Int, position: Int
        ): ViewHolder {
            return if (null == convertView) {

                ViewHolder(context, parent, layoutId, position)
            } else {
                var holder = convertView.tag as ViewHolder
                holder.mPosition = position

                holder
            }
        }
    }

}