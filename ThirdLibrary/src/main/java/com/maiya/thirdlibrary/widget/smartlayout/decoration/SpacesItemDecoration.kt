package com.maiya.thirdlibrary.widget.smartlayout.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/9 15:41
 */
class SpacesItemDecoration constructor(hSpace:Int,vSpace:Int) : RecyclerView.ItemDecoration() {

    private var hSpace=hSpace
    private var vSpace=vSpace

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left=hSpace
        outRect.right=hSpace
        outRect.bottom=vSpace
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = vSpace
    }


}