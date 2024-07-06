package com.zhangsheng.shunxin.information.adapter;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.constant.Constants;
import com.zhangsheng.shunxin.information.holders.InfoStreamBigPicAdHolder;
import com.zhangsheng.shunxin.information.holders.InfoStreamBigPicHolder;
import com.zhangsheng.shunxin.information.holders.InfoStreamEmptyHolder;
import com.zhangsheng.shunxin.information.holders.InfoStreamLeftTextRightPicHolder;
import com.zhangsheng.shunxin.information.holders.InfoStreamTextHolder;
import com.zhangsheng.shunxin.information.holders.InfoStreamThreePicHolder;
import com.zhangsheng.shunxin.ad.AdConstant;

import java.util.ArrayList;
import java.util.List;


/**
 * @author xiangzhenbiao
 * @since 2019/5/9 15:06
 */
public class InfoStreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "InfoStreamAdapter";


    /**
     * 今日头条用[1,9]
     */
    public static final int EMPTY_ITEM_TYPE = 0;
    public static final int INFO_STREAM_ONE_PIC_ITEM_TYPE = 1;
    public static final int INFO_STREAM_THREE_PIC_ITEM_TYPE = 2;
    public static final int INFO_STREAM_LEFT_TEXT_RIGHT_PIC_ITEM_TYPE = 3;
    public static final int INFO_STREAM_TEXT_ITEM_TYPE = 4;
    public static final int INFO_STREAM_IMAGE_AD = 101;
    public static final int ITEM_TYPE_VIDEO_CARD = 7;


    /**
     * 总数据
     */
    public  List<InfoBean.DataBean> mList = new ArrayList<>();


    private FragmentActivity context;
    private Boolean isPull=false;
    private boolean addVideoSuccess=false;

    public InfoStreamAdapter(FragmentActivity context) {
        this.context = context;
    }
    public void addData(List<InfoBean.DataBean> list, Boolean isClear, Boolean firstPage,String code) {
        isPull=isClear;
        if(isClear){
            mList.clear();
        }
        mList.addAll(list);


        insertAd(mList);
        Constants.mList=list;
        notifyDataSetChanged();

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
            case INFO_STREAM_ONE_PIC_ITEM_TYPE:
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info_big_pic_layout, viewGroup,false);
                viewHolder = new InfoStreamBigPicHolder(inflate);
                break;
            case INFO_STREAM_THREE_PIC_ITEM_TYPE:
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info_three_pic_layout, viewGroup,false);
                viewHolder = new InfoStreamThreePicHolder(inflate);
                break;
            case INFO_STREAM_LEFT_TEXT_RIGHT_PIC_ITEM_TYPE:
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info_left_text_right_pic_layout,viewGroup,false);
                viewHolder = new InfoStreamLeftTextRightPicHolder(inflate);
                break;
            case INFO_STREAM_TEXT_ITEM_TYPE:
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info_text_layout,viewGroup,false);
                viewHolder = new InfoStreamTextHolder(inflate);
                break;
            case INFO_STREAM_IMAGE_AD:
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info_big_pic_ad_layout,viewGroup,false);
                viewHolder = new InfoStreamBigPicAdHolder(inflate);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (mList == null) {
            return;
        }
        Object object = mList.get(position);

        if (position == 0) {
            if (isPull) {
                if (viewHolder instanceof InfoStreamLeftTextRightPicHolder) {
                    InfoBean.DataBean dataBean = (InfoBean.DataBean) object;
                    InfoStreamLeftTextRightPicHolder infoStreamLeftTextRightPicHolder = (InfoStreamLeftTextRightPicHolder) viewHolder;
                    infoStreamLeftTextRightPicHolder.setData(dataBean,(Activity) context);
                }else if (viewHolder instanceof InfoStreamEmptyHolder) {
                    InfoBean.DataBean dataBean = (InfoBean.DataBean) object;
                    InfoStreamEmptyHolder infoStreamEmptyHolder = (InfoStreamEmptyHolder) viewHolder;
        //            infoStreamEmptyHolder.setData(dataBean);
                }
            }else{
                onBindMyViewHolder(viewHolder,object);
            }
        } else {

            onBindMyViewHolder(viewHolder,object);

        }
    }

    private void onBindMyViewHolder(RecyclerView.ViewHolder viewHolder,Object object) {
        if (viewHolder instanceof InfoStreamBigPicHolder) {
            InfoBean.DataBean dataBean = (InfoBean.DataBean) object;
            InfoStreamBigPicHolder infoStreamOnePicHolder = (InfoStreamBigPicHolder) viewHolder;
            infoStreamOnePicHolder.setData(dataBean,context);
        }else if (viewHolder instanceof InfoStreamThreePicHolder) {
            InfoBean.DataBean dataBean = (InfoBean.DataBean) object;
            InfoStreamThreePicHolder infoStreamThreePicHolder = (InfoStreamThreePicHolder) viewHolder;
            infoStreamThreePicHolder.setData(dataBean,(Activity) context);
        }else if (viewHolder instanceof InfoStreamLeftTextRightPicHolder) {
            InfoBean.DataBean dataBean = (InfoBean.DataBean) object;
            InfoStreamLeftTextRightPicHolder infoStreamLeftTextRightPicHolder = (InfoStreamLeftTextRightPicHolder) viewHolder;
            infoStreamLeftTextRightPicHolder.setData(dataBean,(Activity) context);
        }else if (viewHolder instanceof InfoStreamTextHolder) {
            InfoBean.DataBean dataBean = (InfoBean.DataBean) object;
            InfoStreamTextHolder infoStreamTextHolder = (InfoStreamTextHolder) viewHolder;
            infoStreamTextHolder.setData(dataBean,(Activity) context);
        }else if (viewHolder instanceof InfoStreamEmptyHolder) {
            InfoBean.DataBean dataBean = (InfoBean.DataBean) object;
            InfoStreamEmptyHolder infoStreamEmptyHolder = (InfoStreamEmptyHolder) viewHolder;
//            infoStreamEmptyHolder.setData(dataBean);
        } else if(viewHolder instanceof InfoStreamBigPicAdHolder) {
            InfoBean.DataBean dataBean = (InfoBean.DataBean) object;
            InfoStreamBigPicAdHolder infoStreamAdHolder = (InfoStreamBigPicAdHolder) viewHolder;
            infoStreamAdHolder.setData(dataBean,(Activity) context);
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
        if (object instanceof InfoBean.DataBean) {

            InfoBean.DataBean dataBean = (InfoBean.DataBean) object;
            if (dataBean != null) {
                int coverMode = dataBean.getCover_mode();
                if (position == 0) {
                    if (isPull) {
                        return INFO_STREAM_LEFT_TEXT_RIGHT_PIC_ITEM_TYPE;
                    }else{
                        int itemId=getMyItemId(coverMode,dataBean);
                        return itemId;
                    }
                } else {
                    int itemId=getMyItemId(coverMode,dataBean);
                    return itemId;
                }
            }

        }


        return EMPTY_ITEM_TYPE;
    }

    private int  getMyItemId(int coverMode,InfoBean.DataBean dataBean) {

        if (coverMode == 0) {
            return INFO_STREAM_TEXT_ITEM_TYPE;
        } else if (coverMode == 1) {
            return INFO_STREAM_ONE_PIC_ITEM_TYPE;
        } else if (coverMode == 2) {
            return INFO_STREAM_THREE_PIC_ITEM_TYPE;
        } else if (coverMode == 3) {
            return INFO_STREAM_LEFT_TEXT_RIGHT_PIC_ITEM_TYPE;
        } else if (coverMode == InfoBean.COVER_MODE_AD) {
            return INFO_STREAM_IMAGE_AD;
        }else if (coverMode == InfoBean.COVER_MODE_VIDEO) {

            return ITEM_TYPE_VIDEO_CARD;
        }
        return EMPTY_ITEM_TYPE;
    }

    private void insertAd(List<InfoBean.DataBean> dataList) {
        if(dataList == null || dataList.isEmpty()) {
            return;
        }

        String[] slots = {AdConstant.INSTANCE.getSLOT_BIGLIST11(), AdConstant.INSTANCE.getSLOT_BIGLIST12(), AdConstant.INSTANCE.getSLOT_BIGLIST13()};
        for(int i = 1; i <= dataList.size(); i += 5) {
            int slotIndex = ((i - 1) / 5) % 3;
            if(addVideoSuccess){
                if(i>1){
                    insertAdOnIndex(dataList, i+1, slots[slotIndex]);
                }else{
                    insertAdOnIndex(dataList, i, slots[slotIndex]);
                }
            }else{
                insertAdOnIndex(dataList, i, slots[slotIndex]);

            }
        }
    }

    private void insertAdOnIndex(List<InfoBean.DataBean> dataList, int index, String slot) {
        if(dataList.size() < index) {
            return;
        } else if(dataList.size() == index) {
            InfoBean.DataBean dataBean = new InfoBean.DataBean();
            dataBean.setAdSlot(slot);
            dataBean.setCover_mode(InfoBean.COVER_MODE_AD);
        } else {
            InfoBean.DataBean dataBean = dataList.get(index);
            if(dataBean.getCover_mode() != InfoBean.COVER_MODE_AD) {
                InfoBean.DataBean newBean = new InfoBean.DataBean();
                newBean.setAdSlot(slot);
                newBean.setCover_mode(InfoBean.COVER_MODE_AD);
                dataList.add(index, newBean);
            }
        }
    }



}
