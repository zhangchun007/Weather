package com.zhangsheng.shunxin.information.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maiya.thirdlibrary.utils.CacheUtil;
import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.bean.TabBean;
import com.zhangsheng.shunxin.information.constant.Constants;

import java.util.List;


/**
 * @Author:liupengbing
 * @Data: 2020/5/22 10:56 AM
 * @Email:aliupengbing@163.com
 */
public class DragAdapter extends BaseAdapter {
    /** TAG*/
    private final static String TAG = "DragAdapter";
    /** 是否显示底部的ITEM */
    private boolean isItemShow = false;
    private Context context;
    /** 控制的postion */
    private int holdPosition;
    /** 是否改变 */
    private boolean isChanged = false;
    /** 列表数据是否改变 */
    private boolean isListChanged = false;
    /** 是否可见 */
    boolean isVisible = true;
    /** 可以拖动的列表（即用户选择的频道列表） */
    public List<TabBean> channelList;
    /** TextView 频道内容 */
    private TextView item_text;
    /** 要删除的position */
    public int remove_position = -1;
    /** 是否是用户频道 */
    private boolean isUser = false;
    private Boolean delIconBoolean=false;
    private int curPos=0;

    public DragAdapter(Context context, List<TabBean> channelList, boolean isUser) {
        this.context = context;
        this.channelList = channelList;
        this.isUser = isUser;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public TabBean getItem(int position) {
        // TODO Auto-generated method stub
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_mygridview_item, null);
        ImageView icon_delete =  view.findViewById(R.id.icon_delete);
        if(delIconBoolean) {
            if ((position != 0)) {
                icon_delete.setVisibility(View.VISIBLE);
            }
        }else{
            icon_delete.setVisibility(View.GONE);
        }
        item_text = (TextView) view.findViewById(R.id.text_item);
        FrameLayout fl_subscribe = view.findViewById(R.id.fl_subscribe);
        TabBean channel = getItem(position);
        item_text.setText(channel.title);
        if(isListChanged){
             if(channelList!=null&&channelList.size()>0){
                 int size=channelList.size();
                 if(position==size-1){
                     item_text.setTextColor(Color.parseColor("#2287F5"));
                 }else{
                     item_text.setTextColor(Color.parseColor("#222222"));
                 }
             }

        }else{
            if(curPos==position){
                item_text.setTextColor(Color.parseColor("#2287F5"));
            }else{
                item_text.setTextColor(Color.parseColor("#222222"));
            }
        }

        if(isUser){
            if ((position == 0)){
                item_text.setEnabled(false);
            }
        }
        if (isChanged && (position == holdPosition) && !isItemShow) {
            fl_subscribe.setVisibility(View.GONE);
            item_text.setSelected(true);
            item_text.setEnabled(true);
            isChanged = false;
        }
        if (!isVisible && (position == -1 + channelList.size())) {
            fl_subscribe.setVisibility(View.GONE);
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if(remove_position == position){
            fl_subscribe.setVisibility(View.GONE);
        }

        return view;
    }

    /** 添加频道列表 */
    public void addItem(TabBean channel) {
        channelList.add(channel);
        isListChanged = true;
        notifyDataSetChanged();

        saveLocalChannel();
    }

    private void saveLocalChannel() {
        String jsonString = new Gson().toJson(channelList);
//        Log.w("lpb","dialog jsonString------>"+jsonString);
        CacheUtil.INSTANCE.putObj(Constants.SP_INFO_LOCAL_ORDER_CHANNEL,jsonString);
    }

    /** 添加删除icon */
    public void addDelteIcon(Boolean delIconBoolean) {
        this.delIconBoolean=delIconBoolean;
        notifyDataSetChanged();
    }
    /** 设置选中状态 */
    public void setChannelTextColor(int curPos) {
        this.curPos=curPos;
        notifyDataSetChanged();
    }
    /** 拖动变更频道排序 */
    public void exchange(int dragPostion, int dropPostion) {
        holdPosition = dropPostion;
        TabBean dragItem = getItem(dragPostion);
        Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
        if(dropPostion<0||dragPostion<0){
            return;
        }
        if (dragPostion < dropPostion) {
            channelList.add(dropPostion + 1, dragItem);
            channelList.remove(dragPostion);
        } else {
            channelList.add(dropPostion, dragItem);
            channelList.remove(dragPostion + 1);
        }
        isChanged = true;
        isListChanged = true;
        notifyDataSetChanged();

        saveLocalChannel();
    }

    /** 获取频道列表 */
    public List<TabBean> getChannnelLst() {
        return channelList;
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        isListChanged = true;
        notifyDataSetChanged();
        saveLocalChannel();
    }

    /** 设置频道列表 */
    public void setListDate(List<TabBean> list) {
        channelList = list;
    }

    /** 获取是否可见 */
    public boolean isVisible() {
        return isVisible;
    }

    /** 排序是否发生改变 */
    public boolean isListChanged() {
        return isListChanged;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    /** 显示放下的ITEM */
    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }

}
