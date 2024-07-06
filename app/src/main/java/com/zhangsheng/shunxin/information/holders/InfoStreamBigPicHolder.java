package com.zhangsheng.shunxin.information.holders;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.utils.ActivityUtil;
import com.zhangsheng.shunxin.information.utils.AppTimeUtils;
import com.zhangsheng.shunxin.information.utils.ImgUtil;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.utils.ActivityUtil;
import com.zhangsheng.shunxin.information.utils.AppTimeUtils;
import com.zhangsheng.shunxin.information.utils.ImgUtil;

import java.util.List;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 1:29 PM
 * @Email:aliupengbing@163.com
 */
public class InfoStreamBigPicHolder extends RecyclerView.ViewHolder {
    private final TextView tv_title;
    private final TextView tv_source;
    private final ImageView iv_cover_image;
    private final LinearLayout ll_item;
    private final TextView tv_time;
    private final ImageView iv_video;


    public InfoStreamBigPicHolder(@NonNull View itemView) {
        super(itemView);
        //view
        tv_title=itemView.findViewById(R.id.tv_title);
        iv_cover_image=itemView.findViewById(R.id.iv_cover_image);
        tv_source=itemView.findViewById(R.id.tv_source);
        ll_item=itemView.findViewById(R.id.ll_item);
        tv_time=itemView.findViewById(R.id.tv_time);
        iv_video=itemView.findViewById(R.id.iv_video);
    }

    public void setData(InfoBean.DataBean dataBean, FragmentActivity acticity) {
        if(dataBean==null){
            return;
        }
        tv_title.setText(dataBean.getTitle());
        tv_source.setText(AppTimeUtils.getInfoPublicTime(dataBean.getPublish_time())+" "+dataBean.getSource());
        if(dataBean.isHas_video()){
            iv_video.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);
            tv_time.setText(AppTimeUtils.getHHmmssTime(dataBean.video_duration()));
        }else{
            iv_video.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);
        }
        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.getInstance().skipInfoDetailsActivity(dataBean,acticity);

            }
        });


        List<InfoBean.DataBean.CoverImageListBean> imageList = dataBean.getCover_image_list();
        if (imageList == null || imageList.size() == 0) {
            return;
        }
        if (!TextUtils.isEmpty(imageList.get(0).getUrl())) {
            ImgUtil.loadImg(iv_cover_image,itemView.getContext(),imageList.get(0).getUrl());
        }




    }

}
