package com.maiya.thirdlibrary.widget.smartlayout.recycleview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.R
import com.maiya.thirdlibrary.widget.smartlayout.adapter.BaseRecyclerViewAdapter
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.layoutmanager.CenterLayoutManager
import com.maiya.thirdlibrary.widget.smartlayout.layoutmanager.FullyStaggeredGridLayoutManager
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import kotlin.math.abs

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/4/15 11:55
 */
class SmartRecycleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RecyclerView(context, attrs, defStyleAttr) {
    private var listener: SmartRecycleListener? = null
    private var datas: MutableList<Any>? = null
    private var layout: Int? = null
    private var isScrollChild = false
    private var layout_type = 0
    private var vertical_divider: Int = -1
    private var horientation_divider: Int = -1
    private var item_count = 3
    private var scrollControl = 0  //0正常 1左右滑动控制 2上下滑动控制
    var VERTICAL = 0
    var HORIZONTAL = 1
    var VERTICAL_WATERFALL = 2
    var VERTICAL_GRID = 3
    var HORIZONTAL_GRID = 4
    var HORIZONTAL_WATERFALL = 5
    var CENTER_SCROLL = 6

    init {
        initAttrs(attrs)
        initRecycle()
    }

    private fun initRecycle() {
        layout?.let {
            this.adapter = SmartAdapter(getData(), layout.nN())
        }
        if (isScrollChild) {
            this.setHasFixedSize(true)
            this.isNestedScrollingEnabled = false
        }

        when (layout_type) {
            VERTICAL -> {
                this.layoutManager = LinearLayoutManager(context)
            }
            HORIZONTAL -> {

                this.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            VERTICAL_WATERFALL -> {
                var manager = FullyStaggeredGridLayoutManager(
                        item_count,
                        StaggeredGridLayoutManager.VERTICAL
                    );
                manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                this.layoutManager = manager
            }

            HORIZONTAL_WATERFALL -> {
                var manager = FullyStaggeredGridLayoutManager(
                        item_count,
                        StaggeredGridLayoutManager.HORIZONTAL
                    );
                manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                this.layoutManager = manager
            }

            HORIZONTAL_GRID -> {
                this.layoutManager =
                    GridLayoutManager(context, item_count, LinearLayoutManager.VERTICAL, false)
            }
            VERTICAL_GRID -> {
                this.layoutManager =
                    GridLayoutManager(context, item_count, LinearLayoutManager.HORIZONTAL, false)
            }

            CENTER_SCROLL -> {
                this.layoutManager = CenterLayoutManager(
                    context
                ).apply {
                    this.orientation =
                        if (scrollControl == 1) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL
                }
            }
        }
        if (vertical_divider != -1) {
            this.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(
                        ContextCompat.getDrawable(context, vertical_divider).nN()
                    )
                })
        }
        if (horientation_divider != -1) {
            this.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.HORIZONTAL
                ).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            context,
                            horientation_divider
                        ).nN()
                    )
                })
        }
    }


    private var mLastX = 0f
    private var mLastY = 0f
    private var xDistance = 0f
    private var yDistance = 0f
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (scrollControl != 0) {
            parent.requestDisallowInterceptTouchEvent(false)
            var y = ev.rawY
            var x = ev.rawX
            ev?.run {
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        xDistance = 0f
                        yDistance = 0f
                        mLastX = x
                        mLastY = y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        yDistance += abs(y - mLastY)
                        xDistance += abs(x - mLastX)
                        if (scrollControl == 1) {
                            if (yDistance > xDistance) {
                                parent.requestDisallowInterceptTouchEvent(false)
                            } else {
                                parent.requestDisallowInterceptTouchEvent(true)
                            }
                        } else {
                            if (xDistance > yDistance) {
                                parent.requestDisallowInterceptTouchEvent(false)
                            } else {
                                parent.requestDisallowInterceptTouchEvent(true)
                            }
                        }
                        mLastX = x
                        mLastY = y
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)

    }


    fun setSmartListener(listener: SmartRecycleListener) {
        this.listener = listener
    }


    fun getData(): MutableList<Any> {
        if (datas == null) {
            datas = ArrayList<Any>()
        }
        return datas!!
    }

    fun notifyData(data: List<Any>) {
        Try {
            listener?.datas(data)
            getData().clear()
            getData().addAll(data)
            this.post {
                this.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SmartRecycleView)
        layout_type = ta.getInt(R.styleable.SmartRecycleView_LayoutManager, 0)
        layout = ta.getResourceId(R.styleable.SmartRecycleView_item_layout, 0)
        isScrollChild = ta.getBoolean(R.styleable.SmartRecycleView_isScrollChild, false)
        vertical_divider = ta.getResourceId(R.styleable.SmartRecycleView_vertical_divider, -1)
        horientation_divider =
            ta.getResourceId(R.styleable.SmartRecycleView_horizontal_divider, -1)
        item_count = ta.getInteger(R.styleable.SmartRecycleView_item_count, -1)
        scrollControl = ta.getInteger(R.styleable.SmartRecycleView_ScrollContol, 0)
        ta.recycle()
    }


    inner class SmartAdapter(ds: MutableList<Any>, lt: Int) :
        BaseRecyclerViewAdapter<Any>(ds, lt) {
        override fun bindDataToItemView(holder: SmartViewHolder?, bean: Any, position: Int) {
            listener?.let { it.AutoAdapter(holder.nN(), bean, position) }
        }
    }
}