package com.zhangsheng.shunxin.weather.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.maiya.thirdlibrary.ext.isVisible
import com.zhangsheng.shunxin.weather.widget.HomeAnimatorView
import org.libpag.PAGFile
import org.libpag.PAGView

object CustomAnimationUtil {

    fun alphaAnim(weather_bg: View, weather_bg_current: HomeAnimatorView) {
        val animator = ObjectAnimator.ofFloat(weather_bg, "alpha", 1f, 0f)
            .setDuration(500)
        animator.interpolator = AccelerateInterpolator()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                weather_bg?.isVisible(false)
                weather_bg_current?.setLayerType(View.LAYER_TYPE_NONE, null)
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        animator.start()
    }

    /**
     * pag动画渐变
     */
    fun pagAnimChange(
        currentImage: PAGView,
        backImage: PAGView,
        res: PAGFile,
    ) {
        currentImage.stop()
        backImage.progress = currentImage.progress
        backImage.play()

        currentImage.progress = 0.0
        currentImage.file = res

        val animator =
            ObjectAnimator.ofFloat(backImage, "alpha", 1f, 0f)
                .setDuration(500)
        animator.interpolator = AccelerateInterpolator()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                currentImage?.play()
                currentImage.postDelayed(Runnable {
//                    currentImage.translationX = 0f
                }, 100)
            }

            override fun onAnimationEnd(animator: Animator) {
                backImage?.file = res
                backImage.postDelayed(Runnable {
//                    backImage.translationX = 0f
                    backImage.stop()
                }, 100)

            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationRepeat(animator: Animator) {
            }
        })
        animator.start()
    }


}