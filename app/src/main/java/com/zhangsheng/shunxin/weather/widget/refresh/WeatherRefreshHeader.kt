package com.zhangsheng.shunxin.weather.widget.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/23 15:27
 */
class WeatherRefreshHeader@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :LinearLayout(context,attrs,defStyleAttr) ,RefreshHeader {
//    private  var tv:TextView
//    private  var img:ImageView

    init {

//        gravity = Gravity.CENTER
//        orientation= VERTICAL
//        tv= TextView(context)
//        tv.text="下拉立即刷新"
//        tv.gravity=Gravity.CENTER
//        tv.setTextColor(Color.parseColor("#ffffff"))
//        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10f)
//        img= ImageView(context)
//        img.setImageResource(R.mipmap.icon_refresh_sun)
//
//        addView(img)
//        addView(tv,LayoutParams.WRAP_CONTENT,DisplayUtil.dip2px(context,20f))
//        minimumHeight=DisplayUtil.dip2px(context,70f)
    }


    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
//        img.clearAnimation()
//        //切换成功
//        if (success){
//            img.setImageResource(R.mipmap.icon_refresh_ok)
//            tv.text="更新成功"
//
//        }else{
//            tv.text="刷新失败"
//        }

        return 100

    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun getView(): View= this

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
//        img.startAnimation(AnimationUtils.loadAnimation(context,R.anim.refresh_route))
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
//        when (newState) {
//            RefreshState.PullDownToRefresh -> {
//                img.setImageResource(R.mipmap.icon_refresh_sun)
//                tv.text="下拉立即刷新"
//            }
//            RefreshState.Refreshing -> {
//                tv.text="正在刷新..."
//            }
//            RefreshState.ReleaseToRefresh -> {
//                tv.text="释放立即刷新"
//            }
//        }

    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun isSupportHorizontalDrag(): Boolean =false
}