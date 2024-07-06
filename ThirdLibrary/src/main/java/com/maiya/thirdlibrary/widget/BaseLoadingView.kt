package com.maiya.thirdlibrary.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

abstract class BaseLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    companion object{
        val LOADING="baseLoading"
        val ERROR="baseError"
    }

    open fun setViewStatus(status: String) {}

    open fun setHintText(text:String){
    }
    open  var getErrorClickView: View? = null


}