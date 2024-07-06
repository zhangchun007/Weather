package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.view.forEach
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.nN
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.bottom.BottomBarItem
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.utils.AnimationUtil
import java.util.*

/**
 * 说明：自定义首页底部tabview
 * 作者：刘鹏
 * 添加时间：2021/4/28 15:58
 * 修改人：liupe
 * 修改时间：2021/4/28 15:58
 */
open class BottomTabLayout : ViewGroup {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mItems: LinkedHashMap<String, BottomBarItem>? = null
    private var mKeyList: MutableList<String>? = null
    private var mSelectedItem: BottomBarItem? = null
    private var mCurrentSubView: View? = null

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val horizontalPadding = paddingLeft + paddingRight
        val verticalPadding = paddingTop + paddingBottom
        val contentWidth = widthSize - horizontalPadding
        val count = childCount
        var height = 0
        if (count != 0) {
            val childWidth = contentWidth / count
            for (i in 0 until count) {
                val child = getChildAt(i)
                val lp =
                    child.layoutParams as MarginLayoutParams
                val childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(
                    widthMeasureSpec,
                    horizontalPadding,
                    childWidth
                )
                val childHeightMeasureSpec = ViewGroup.getChildMeasureSpec(
                    heightMeasureSpec,
                    verticalPadding,
                    lp.height
                )
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
                val measuredHeight = child.measuredHeight
                if (height < measuredHeight) {
                    height = measuredHeight
                }
            }
        }
        setMeasuredDimension(
            View.resolveSize(widthSize, widthMeasureSpec),
            View.resolveSize(height + verticalPadding, heightMeasureSpec)
        )
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        val count = childCount
        val horizontalPadding = paddingLeft + paddingRight
        val contentWidth = measuredWidth - horizontalPadding
        if (count != 0) {
            val childWidth = contentWidth / count
            for (i in 0 until count) {
                val child = getChildAt(i)
                val left = i * childWidth + paddingLeft
                val top = paddingTop
                val bottom = measuredHeight - paddingBottom
                val right = left + childWidth
                child.layout(left, top, right, bottom)
            }
        }
    }

    open fun setItems(items: List<BottomBarItem>?) {
        if (items == null) {
            return
        }
        val clickListener = OnClickListener OnClickListener@{
            if (mCurrentSubView === it) {
                return@OnClickListener
            }
            it.isClickable = false
            it.postDelayed({ it.isClickable = true }, 1000L)
            val cmd: String = (it.tag as BottomBarItem).getEventCMD().nN()
            mOnItemClickListener?.onClick(cmd, it, it.getTag(R.id.bottom_position) as Int)
        }
        if (mItems == null) {
            mItems = LinkedHashMap<String, BottomBarItem>(5)
            mKeyList = ArrayList(5)
        }
        for (i in items.nN().indices) {
            val item: BottomBarItem = items[i]
            val view: View = item.inflateView(this).nN()
            view.setTag(R.id.bottom_position, i)
            view.setOnClickListener(clickListener)
            item.setSelected(item.isSelected())
            val tag: String = item.getEventCMD().nN()
            mItems.nN()[tag] = item
            mKeyList!!.add(tag)
            addView(view)
        }
    }

    /**
     * 说明：重新设置底部tab视图，移除原有视图
     * 作者：刘鹏
     * 添加时间：2020-04-22 09:42
     * 修改人：liupe
     * 修改时间：2020-04-22 09:42
     */
    open fun resetItems(items: List<BottomBarItem>?) {
        if (items == null) {
            return
        }
        removeAllViews()
        mItems = null
        setItems(items)
    }


    open fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    open fun setSelectedItemView(tag: String?) {
        mSelectedItem?.setSelected(false)
        mSelectedItem = null
        val bottomBarItem: BottomBarItem? = mItems!![tag.nN()]
        if (bottomBarItem != null) {
            bottomBarItem.setSelected(true)
            mSelectedItem = bottomBarItem
            mCurrentSubView = bottomBarItem.getView()
            bottomBarItem.getClickEvent()?.let {
                clickReport(it)
            }
            getAppModel().appPageIndex = mSelectedItem.nN().getEventCMD().nN(BottomBarItem.CMD_WEATHER)
        }
    }

    open fun setSelectedItemView(position: Int) {
        if (mKeyList != null && mKeyList!!.size > position) {
            setSelectedItemView(mKeyList!![position])
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        return LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): LayoutParams? {
        return LayoutParams(p)
    }

    fun hideBottomBar(hide: Boolean = false) {
        this.isVisible(!hide)
        if (hide && mSelectedItem?.getPosition() == 0) {
            val translateAnimation = AnimationUtil.moveToViewBottom(400)
            translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    this@BottomTabLayout.isVisible(mSelectedItem?.getPosition() != 0)
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
            this@BottomTabLayout.startAnimation(translateAnimation)
        } else {
            this@BottomTabLayout.startAnimation(AnimationUtil.moveToViewLocation())
        }
    }


    class LayoutParams : MarginLayoutParams {
        constructor(c: Context?, attrs: AttributeSet?) : super(
            c,
            attrs
        ) {
        }

        constructor(source: ViewGroup.LayoutParams?) : super(source) {}
        constructor(source: MarginLayoutParams?) : super(source) {}
        constructor(width: Int, height: Int) : super(width, height) {}
    }

    interface OnItemClickListener {
        fun onClick(cmd: String?, v: View?, position: Int)
    }


}