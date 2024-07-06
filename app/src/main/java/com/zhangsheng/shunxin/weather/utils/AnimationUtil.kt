package com.zhangsheng.shunxin.weather.utils

import android.animation.*
import android.os.Build
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.widget.shapview.ShapeRelativeLayout


object AnimationUtil {
    private val TAG = AnimationUtil::class.java.simpleName

    /**
     * 添加Item动画
     * @param v
     * @param height
     * @return
     */
    fun addItem(v: View?, height: Int?): ValueAnimator {
        v?.visibility = View.VISIBLE
        val animator = ValueAnimator.ofInt(0, height.nN(0))
        animator.duration = 2000
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            v?.layoutParams?.height = value
            v?.layoutParams = v.nN().layoutParams
        }

        return animator
    }

    /**
     * 删除Item
     * @param v
     * @param height
     * @return
     */
    fun deleteItem(v: View?, height: Int?): ValueAnimator {
        v?.visibility = View.GONE
        val animator = ValueAnimator.ofInt(height.nN(0), 0)
        animator.duration = 1000
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            if (value == 0) {
                v?.visibility = View.GONE
            }
            v?.layoutParams?.height = value
            v?.layoutParams = v?.layoutParams
        }

        return animator
    }


    /**
     * 拉长动画
     */
    fun toLong(v: View): ScaleAnimation {
        v.visibility = View.VISIBLE
        val scaleAnimation = ScaleAnimation(
            0f, 1f, 1f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        //3秒完成动画
        scaleAnimation.duration = 200
        scaleAnimation.fillAfter = true
        return scaleAnimation
    }

    /**
     * 缩短动画
     */
    fun toShort(v: View): ScaleAnimation {
        v.visibility = View.GONE
        val scaleAnimation = ScaleAnimation(
            1f, 0f, 1f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        //3秒完成动画
        scaleAnimation.duration = 200
        scaleAnimation.fillAfter = true
        return scaleAnimation
    }


    /**
     * 变大动画
     * @return
     */
    fun toBig(): ScaleAnimation {
        val scaleAnimation = ScaleAnimation(
            1f, 1.2f, 1f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        //3秒完成动画
        scaleAnimation.duration = 200
        scaleAnimation.fillAfter = true
        return scaleAnimation
    }

    /**
     * 变小动画
     * @return
     */
    fun toSmall(): ScaleAnimation {
        val scaleAnimation = ScaleAnimation(
            1.2f, 1f, 1.2f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        //3秒完成动画
        scaleAnimation.duration = 300
        scaleAnimation.fillAfter = true
        return scaleAnimation
    }

    /**
     * 变小动画
     * @return
     */
    fun SmalltoShow(): ScaleAnimation {
        val scaleAnimation = ScaleAnimation(
            0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
        )

        scaleAnimation.duration = 800
        scaleAnimation.fillAfter = true
        return scaleAnimation
    }


    /**
     * 渐变动画 从无到有
     */
    fun BtnShow(time: Int): AlphaAnimation {

        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = time.toLong()
        alphaAnimation.fillAfter = true



        return alphaAnimation
    }

    /**
     * 渐变动画 从有到无
     */
    fun BtnMsis(time: Int): AlphaAnimation {

        val alphaAnimation = AlphaAnimation(1f, 0f)
        alphaAnimation.duration = time.toLong()
        alphaAnimation.fillAfter = true
        return alphaAnimation
    }

    /**
     * 渐变动画 文字切换动画
     */
    fun BtnChange(time: Int, view: TextView, text: String) {

        val alphaAnimation = AlphaAnimation(1f, 0f)
        alphaAnimation.duration = time.toLong()
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                view.text = text
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        view?.startAnimation(alphaAnimation)
    }

    /**
     * 从左到右隐藏
     *
     * @return
     */
    fun leftToRightHidden(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        mHiddenAction.duration = 500
        mHiddenAction.fillAfter = true
        return mHiddenAction
    }

    fun leftToRightHidden(view: View) {
        var objectAnimatorX =
            ObjectAnimator.ofFloat(view, "translationX", (0.8 * view.width).toFloat())
        objectAnimatorX.duration = 500
        objectAnimatorX.start()
    }


    /**
     * 从右到左显示
     *
     * @return
     */
    fun rightToleftShow(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.8f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        mHiddenAction.duration = 500
        mHiddenAction.fillAfter = true
        return mHiddenAction
    }

    fun rightToleftShow(view: View) {
        var objectAnimatorX = ObjectAnimator.ofFloat(view, "translationX", 0f)
        objectAnimatorX.duration = 500
        objectAnimatorX.start()
    }


    /**
     * 从右到左隐藏
     *
     * @return
     */
    fun rightToleftHidden(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        mHiddenAction.duration = 500
        return mHiddenAction
    }


    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    fun bottomToHidden(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 1.0f
        )
        mHiddenAction.duration = 200
        return mHiddenAction
    }

    fun moveToViewBottom(time: Long): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 1.0f
        )
        mHiddenAction.duration = time
        return mHiddenAction
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    fun moveToViewLocation(duration: Long = 500L): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        mHiddenAction.duration = duration
        return mHiddenAction
    }

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    fun moveToViewBottom(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 1.0f
        )
        mHiddenAction.duration = 500
        return mHiddenAction
    }

    /**
     * 从底部展示
     *
     * @return
     */
    fun bottomToShow(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        mHiddenAction.duration = 200
        return mHiddenAction
    }

    /**
     * 从控件的顶部移动到所在位置 从顶部显示
     *
     * @return
     */
    fun ToPmoveToViewLocation(): TranslateAnimation {
        val mShowAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        mShowAction.duration = 200
        return mShowAction
    }

    /**
     * 从控件的顶部移动到所在位置 从顶部隐藏
     *
     * @return
     */
    fun TopMoveOut(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f
        )
        mHiddenAction.duration = 200
        return mHiddenAction
    }

    /**
     * 字体抖动
     */
    fun tvShake(view: View, delta: Float = 8f) {

        var pvhTranslateX = PropertyValuesHolder.ofKeyframe(
            View.TRANSLATION_X,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.10f, -delta),
            Keyframe.ofFloat(.26f, delta),
            Keyframe.ofFloat(.42f, -delta),
            Keyframe.ofFloat(.58f, delta),
            Keyframe.ofFloat(.74f, -delta),
            Keyframe.ofFloat(.90f, delta),
            Keyframe.ofFloat(1f, 0f)
        )
        var anim = ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX)
        anim.duration = 500
        anim.start()
    }

    /**
     * 淡入
     */
    fun lightInAnim(view: View) {
        var alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.duration = 200
        view.startAnimation(alphaAnimation)
    }

    /**
     * 淡出
     */
    fun lightOutAnim(view: View) {
        try {
            var alphaAnimation = AlphaAnimation(1f, 0f)
            alphaAnimation.duration = 200
            view.startAnimation(alphaAnimation)
            view.isVisible(false)
        } catch (e: Exception) {
            view?.isVisible(false)
        }
    }

    /**
     * 图片渐变
     */
    fun picChange(currentImage: ImageView, backImage: ImageView, res: Int) {
        currentImage.setImageResource(res)
        val animator =
            ObjectAnimator.ofFloat(backImage, "alpha", 1f, 0f)
                .setDuration(200)
        animator.interpolator = AccelerateInterpolator()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                backImage?.setImageResource(res)
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        animator.start()
    }

    /**
     * lottie动画渐变
     */
    fun animChange(
        currentImage: LottieAnimationView,
        backImage: LottieAnimationView,
        res: String
    ) {
        currentImage.pauseAnimation()
        backImage.progress = currentImage.progress
        backImage.imageAssetsFolder = "images"

        currentImage.progress = 0f
        currentImage.setAnimation(res)
        currentImage.imageAssetsFolder = "images"
        val animator =
            ObjectAnimator.ofFloat(backImage, "alpha", 1f, 0f)
                .setDuration(500)
        animator.interpolator = AccelerateInterpolator()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                backImage?.setAnimation(res)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    currentImage?.playAnimation()
                }
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        animator.start()
    }



    /**
     * 颜色渐变
     */
    fun colorChange(view: View, startColor: Int, endColor: Int, endFunc: () -> Unit) {

        var backgroundColorAnimator = ObjectAnimator.ofObject(
            view,
            "backgroundColor",
            ArgbEvaluator(),
            startColor,
            endColor
        )
        backgroundColorAnimator.duration = 500
        backgroundColorAnimator.start()

        backgroundColorAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                endFunc()
            }
        })


    }

    /**
     * 摇晃动画
     */
    fun startShakeByViewAnim(view: View) {
        val rotateAnimation = RotateAnimation(
            0f,
            5f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            1f
        )
        rotateAnimation.interpolator = CycleInterpolator(5f)
        rotateAnimation.repeatCount = -1
        rotateAnimation.duration = 1000

        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation) {
                p0.cancel()
                view.postDelayed(Runnable {
                    view.startAnimation(p0)
                }, 800)
            }

            override fun onAnimationEnd(p0: Animation?) {
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })

        view.startAnimation(rotateAnimation)
    }

    /**
     * 金币缩放
     */
    fun coinAnim(view: View?) {
        Try {
            var animator = AnimatorSet();//组合动画
            var scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
            var scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
            animator.duration = 400
            animator.interpolator = OvershootInterpolator()
            animator.play(scaleX).with(scaleY)
            animator.start()
        }
    }

    /**
     * 缩放仿刷新动画
     */
    fun refreshAnim(view: View?, finish: () -> Unit) {
        if (view != null) {
            view?.clearAnimation()
            var scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f, 1f, 0f, 1f)
            scaleX.duration = 1000
            scaleX.interpolator = LinearInterpolator()
            scaleX.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    finish()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            scaleX.start()
        }
    }

    /**
     * 金币旋转
     */
    fun coinTrans(view: View?): ObjectAnimator? {
        if (view != null) {
            view?.clearAnimation()
            var scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f, 1f, 0f, 1f, 0f, 1f)
            scaleX.duration = 2000
            scaleX.repeatCount = 5
            scaleX.repeatMode = ValueAnimator.REVERSE
            scaleX.interpolator = LinearInterpolator()
            scaleX.start()
            scaleX.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    view?.scaleX = 1f
                }

                override fun onAnimationCancel(animation: Animator?) {
                    view?.scaleX = 1f
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })


            return scaleX
        }
        return null
    }


    /**
     * 呼吸效果
     */
    fun breath(view: View?, time: Long = 400) {
        view?.let {
            view?.clearAnimation()
            val scaleAnimation = ScaleAnimation(
                1f, 0.8f, 1f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            //3秒完成动画
            scaleAnimation.duration = time
            scaleAnimation.fillAfter = true
            scaleAnimation.repeatMode = Animation.REVERSE
            scaleAnimation.repeatCount = Int.MAX_VALUE
            scaleAnimation.interpolator = LinearInterpolator()
            view.startAnimation(scaleAnimation)
        }
    }


    fun loadLottieAnim(view: LottieAnimationView?, res: String) {
        view?.let {
            view?.progress = 0f
            view?.setAnimation(res)
            view?.loop(true)
            view?.imageAssetsFolder = "images"
            view?.playAnimation()
        }
    }


    fun floatAnim(view: View, delay: Long) {
        view.isVisible(true)
        val animators: MutableList<Animator> = ArrayList()
        val translationYAnim: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "translationY", -10.0f, 10.0f, -10.0f)
        translationYAnim.duration = delay
        translationYAnim.repeatCount = ValueAnimator.INFINITE
        translationYAnim.start()
        animators.add(translationYAnim)

        val btnSexAnimatorSet = AnimatorSet()
        btnSexAnimatorSet.playTogether(animators)
        btnSexAnimatorSet.startDelay = delay
        btnSexAnimatorSet.start()
    }

    fun shakeAnimation(counts: Float): Animation? {
        val rotateAnimation: Animation = RotateAnimation(
            0f,
            20f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.interpolator = CycleInterpolator(counts)
        rotateAnimation.repeatCount = -1
        rotateAnimation.duration = 3000
        return rotateAnimation
    }

    fun startShakeByViewAnim(
        view: View?,
        scaleSmall: Float,
        scaleLarge: Float,
        shakeDegrees: Float,
        duration: Long
    ) {
        if (view == null) {
            return
        }
        //TODO 验证参数的有效性
//由小变大
        val scaleAnim: Animation =
            ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge)
        //从左向右
        val rotateAnim: Animation = RotateAnimation(
            -shakeDegrees,
            shakeDegrees,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnim.duration = duration
        rotateAnim.duration = duration / 10
        rotateAnim.repeatMode = Animation.REVERSE
        rotateAnim.repeatCount = -1
        val smallAnimationSet = AnimationSet(false)
        smallAnimationSet.addAnimation(scaleAnim)
        smallAnimationSet.addAnimation(rotateAnim)
        view.startAnimation(smallAnimationSet)
    }

    fun ChangeCardColor(
        card: ShapeRelativeLayout,
        startColor: IntArray,
        endColor: IntArray,
        duration: Long
    ) {
        var config = card.getConfig()
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            card.exeConfig(config.apply {
                this.startColor = CalculateUtil.colorEvaluate(
                    animation.animatedValue as Float,
                    startColor[0],
                    endColor[0]
                )
                this.endColor = CalculateUtil.colorEvaluate(
                    animation.animatedValue as Float,
                    startColor[1],
                    endColor[1]
                )
            })
        }
        animator.start()
    }


    /**
     * 从底部展示
     *
     * @return
     */
    fun bottomToShow(view: View?, block: () -> Unit = {}) {
        val mHiddenAction = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        mHiddenAction.duration = 800
        mHiddenAction.interpolator = DecelerateInterpolator()
        mHiddenAction.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                block.invoke()
            }

            override fun onAnimationStart(p0: Animation?) {
                view?.alpha = 1f
            }
        })
        view?.startAnimation(mHiddenAction)
    }

    fun startWeatherIconAnimation(view: ImageView?) {
        val translateAnimation: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f
        )
        translateAnimation.interpolator = OvershootInterpolator(1f)
        translateAnimation.duration = 800 //动画持续的时间为10s
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
                view?.alpha = 1f
            }
        })
        view?.startAnimation(translateAnimation)
    }

}