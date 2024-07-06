package com.zhangsheng.shunxin.information.adapter;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.bean.InfoCommendBean;
import com.zhangsheng.shunxin.information.constant.Constants;
import com.zhangsheng.shunxin.information.holders.InfoCommendLeftTextRightPicHolder;
import com.zhangsheng.shunxin.information.holders.InfoStreamEmptyHolder;
import com.zhangsheng.shunxin.information.holders.InfoStreamSmallPicAdHolder;
import com.zhangsheng.shunxin.ad.AdConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangzhenbiao
 * @since 2019/5/9 15:06
 */
public class InfoDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "InfoStreamAdapter";


    /**
     * 今日头条用[1,9]
     */
    public static final int EMPTY_ITEM_TYPE = 0;

    public static final int INFO_STREAM_LEFT_TEXT_RIGHT_PIC_ITEM_TYPE = 3;
    public static final int INFO_STREAM_AD = 100;


    /**
     * 总数据
     */
    public  List<InfoCommendBean.DataBean> mList = new ArrayList<>();

    /**
     * 临时存储数据
     */
    private List<InfoBean.DataBean> mTempList = new ArrayList<>();
    private Context context;
    private boolean mIsWebViewOnBottom;

    public InfoDetailsAdapter(Context context) {
        this.context = context;
    }
    public void addData(List<InfoCommendBean.DataBean> list,Boolean isClear) {
        if(isClear){
            mList.clear();
        }
        mList.addAll(list);
        insertAd(mList);
        notifyDataSetChanged();
        Constants.mCommenList=mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View inflate;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case EMPTY_ITEM_TYPE:
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info_stream_empty, viewGroup,false);
                viewHolder = new InfoStreamEmptyHolder(inflate);
                break;
            case INFO_STREAM_LEFT_TEXT_RIGHT_PIC_ITEM_TYPE:
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info_left_text_right_pic_layout,viewGroup,false);
                viewHolder = new InfoCommendLeftTextRightPicHolder(inflate);
                break;
            case INFO_STREAM_AD:
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info_small_pic_ad_layout,viewGroup,false);
                viewHolder = new InfoStreamSmallPicAdHolder(inflate);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (mList == null) {
            return;
        }
        InfoCommendBean.DataBean dataBean = mList.get(position);
        if (viewHolder instanceof InfoCommendLeftTextRightPicHolder) {
            InfoCommendLeftTextRightPicHolder infoCommendLeftTextRightPicHolder = (InfoCommendLeftTextRightPicHolder) viewHolder;
            infoCommendLeftTextRightPicHolder.setData(dataBean,(Activity) context,position);
        } else if(viewHolder instanceof InfoStreamSmallPicAdHolder) {
            InfoStreamSmallPicAdHolder infoStreamAdHolder = (InfoStreamSmallPicAdHolder) viewHolder;
            infoStreamAdHolder.setData(dataBean,(Activity) context,position);
        }
    }



    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList == null) {
            return EMPTY_ITEM_TYPE;
        }

        if (position > mList.size()) {
            return EMPTY_ITEM_TYPE;
        }

        Object object = mList.get(position);
        if (object == null) {
            return EMPTY_ITEM_TYPE;
        }

        // 今日头条数据
        if (object instanceof InfoCommendBean.DataBean) {

//           3：右图
            InfoCommendBean.DataBean dataBean = (InfoCommendBean.DataBean) object;
            int coverMode = dataBean.getCover_mode();
            if(coverMode == InfoBean.COVER_MODE_AD) {

                return INFO_STREAM_AD;

            } else {
                return INFO_STREAM_LEFT_TEXT_RIGHT_PIC_ITEM_TYPE;
            }
        }


        return EMPTY_ITEM_TYPE;
    }

    private void insertAd(List<InfoCommendBean.DataBean> dataList) {
        if(dataList == null || dataList.isEmpty()) {
            return;
        }
        insertAdOnIndex(dataList, 2, AdConstant.INSTANCE.getSLOT_SMALLXWXQ1());
    }

    private void insertAdOnIndex(List<InfoCommendBean.DataBean> dataList, int index, String slot) {
        if(dataList.size() < index) {
            return;
        } else if(dataList.size() == index) {
            InfoCommendBean.DataBean dataBean = new InfoCommendBean.DataBean();
            dataBean.setAdSlot(slot);
            dataBean.setCover_mode(InfoBean.COVER_MODE_AD);
            dataList.add(dataBean);
        } else {
            InfoCommendBean.DataBean dataBean = dataList.get(index);
            if(dataBean.getCover_mode() != InfoBean.COVER_MODE_AD) {
                InfoCommendBean.DataBean newBean = new InfoCommendBean.DataBean();
                newBean.setAdSlot(slot);
                newBean.setCover_mode(InfoBean.COVER_MODE_AD);
                dataList.add(index, newBean);
            }
        }
    }



}
