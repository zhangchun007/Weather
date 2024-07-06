package com.zhangsheng.shunxin.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.bean.TabBean;
import com.zhangsheng.shunxin.information.bean.TabBean;

import java.util.List;


/**
 * @Author:liupengbing
 * @Data: 2020/5/22 10:59 AM
 * @Email:aliupengbing@163.com
 */
public class OtherAdapter extends BaseAdapter {

    private Context context;
    public List<TabBean> channelList;
    private TextView item_text;
    /** 是否可见 在移动动画完毕之前不可见，动画完毕后可见*/
    boolean isVisible = true;
    /** 要删除的position */
    public int remove_position = -1;
    /** 是否是用户频道 */
    private boolean isUser = false;

    public OtherAdapter(Context context, List<TabBean> channelList , boolean isUser) {
        this.context = context;
        this.channelList = channelList;
        this.isUser = isUser;
    }

    @Override
    public int getCount() {
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public TabBean getItem(int position) {
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_mygridview_item, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        TabBean channel = getItem(position);
        item_text.setText("+"+channel.title);
        if(isUser){
            if ((position == 0) || (position == 1)){
                item_text.setEnabled(false);
            }
        }
        if (!isVisible && (position == -1 + channelList.size())){
            item_text.setVisibility(View.GONE);
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if(remove_position == position){
            item_text.setVisibility(View.GONE);
        }
        return view;
    }

    /** 获取频道列表 */
    public List<TabBean> getChannnelLst() {
        return channelList;
    }

    /** 添加频道列表 */
    public void addItem(TabBean channel) {
        channelList.add(channel);
        notifyDataSetChanged();
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
        // notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        notifyDataSetChanged();
    }
    /** 设置频道列表 */
    public void setListDate(List<TabBean> list) {
        channelList = list;
    }

    /** 获取是否可见 */
    public boolean isVisible() {
        return isVisible;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

}
