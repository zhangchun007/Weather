package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.zhangsheng.shunxin.R
import com.yanzhenjie.recyclerview.swipe.SwipeMenu
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem

class SwipeMenuCreator(val instance: Context) : SwipeMenuCreator {
    override fun onCreateMenu(swipeLeftMenu: SwipeMenu, swipeRightMenu: SwipeMenu, position: Int) {

        val height = ViewGroup.LayoutParams.MATCH_PARENT
        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        run {
            val deleteItem =
                SwipeMenuItem(instance).setBackgroundColor(Color.parseColor("#FF4B4B"))
                    .setImage(R.drawable.ic_icon_city_delete)
                    .setWidth(instance.resources.getDimensionPixelSize(R.dimen.dp_60))
                    .setHeight(height)
            swipeRightMenu.addMenuItem(deleteItem)// 添加一个按钮到右侧侧菜单。
        }
    }
}