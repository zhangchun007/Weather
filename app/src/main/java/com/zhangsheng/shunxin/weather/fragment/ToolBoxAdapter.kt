package com.zhangsheng.shunxin.weather.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.zhangsheng.shunxin.R

/**
 * 说明：实用工具列表对应adapter
 * 作者：刘鹏
 * 添加时间：5/8/21 2:15 PM
 * 修改人：liupe
 * 修改时间：5/8/21 2:15 PM
 */
class ToolBoxAdapter: RecyclerView.Adapter<ToolBoxAdapter.ToolViewHolder>() {

    var toolboxList: List<ToolItemModel>? = null

    var listener: OnItemClickListener? = null

    fun setData(list: List<ToolItemModel>?) {
        toolboxList = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_tool_item, parent, false)
        return ToolViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(toolboxList == null) 0 else toolboxList!!.size
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {

        val tool = toolboxList?.get(position)

        tool?.run {
            holder.ivTool?.setImageResource(toolIcon)
            holder.tvTool?.text = toolTitle
            holder.redpoint?.isVisible(showRed)
            holder.itemView.setSingleClickListener {
                listener?.onItemClick(tool)
            }
        }

    }


    class ToolViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var ivTool: ImageView? = null
        var tvTool: TextView? = null
        var redpoint: View? = null

        init {
            ivTool = itemview.findViewById(R.id.iv_tool)
            tvTool = itemview.findViewById(R.id.tv_tool)
            redpoint = itemview.findViewById(R.id.redpoint)
        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: ToolItemModel)
    }
}