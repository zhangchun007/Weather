package com.zhangsheng.shunxin.ad.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.maiya.thirdlibrary.utils.DisplayUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.ad.AdUtils
import com.zhangsheng.shunxin.weather.widget.TouchScrollView

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/4/16 15:47
 */
class AdvFloatMaterialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ExtAdMaterialView(context, attrs, defStyleAttr) {
    private var currentHeight = 0
    private var animTime: Long = 500
    private var translationXin: ObjectAnimator? = null
    private var translationXout: ObjectAnimator? = null
    private val CHECK_STATE = -1
    private var animManagers = ArrayList<Int>()

    override fun getLayoutId(): Int = R.layout.adv_material_view_float_des

    fun setFloatAdHeight(height: Int) {
        var h = height - DisplayUtil.dip2px(77f)
        if (h < 0) h = AdUtils.floatTopHeight
        if ((currentHeight == 0 || h != AdUtils.floatTopHeight) && h > 0) {
            if (h < DisplayUtil.getScreenHeight() / 4 || h > DisplayUtil.getScreenHeight() * 0.75) return
            layoutParams =
                (layoutParams as CoordinatorLayout.LayoutParams).apply {
                    this.topMargin = h
                }
            currentHeight = h
            AdUtils.floatTopHeight = h
        }
    }


    fun doAnim(state: Int) {
//        if (adv_float == null) return
        if (!animManagers.any { it == state }) animManagers.add(state)
        if (translationXout?.isRunning == true) return
        if (translationXin?.isRunning == true) return
        when (state) {
            TouchScrollView.START_SCROLL -> {
                hindFloatAdvertView()
            }
            TouchScrollView.STOP_SCROLL -> {
                showFloatAdvertView()
            }
            CHECK_STATE -> {
                if (animManagers.isEmpty()) return
                when (animManagers.last()) {
                    TouchScrollView.START_SCROLL -> {
                        hindFloatAdvertView()
                    }
                    TouchScrollView.STOP_SCROLL -> {
                        showFloatAdvertView()
                    }
                }
            }
        }
    }

    private fun hindFloatAdvertView() {
        translationXout = ObjectAnimator.ofFloat(
            this,
            "translationX",
            *floatArrayOf(0.0f, (-(this.measuredWidth + DisplayUtil.dip2px(12f))).toFloat())
        )
        translationXout?.duration = animTime
        translationXout?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                animManagers.remove(TouchScrollView.START_SCROLL)
                doAnim(CHECK_STATE)
            }
        })
        translationXout?.start()


    }

    private fun showFloatAdvertView() {
        translationXin = ObjectAnimator.ofFloat(
            this, "translationX", *floatArrayOf(
                (-(this.measuredWidth + DisplayUtil.dip2px(12f))).toFloat(), 0.0f
            )
        )
        translationXin?.duration = animTime
        translationXin?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                animManagers.remove(TouchScrollView.STOP_SCROLL)
                doAnim(CHECK_STATE)
            }
        })
        translationXin?.start()
    }

    override fun getCloseView(): View? = findViewById(R.id.adv_small_icon_des_close)

}