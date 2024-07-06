package com.zhangsheng.shunxin.information.holders;

import android.app.Activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
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
public class InfoStreamLeftTextRightPicHolder extends RecyclerView.ViewHolder {
    private final TextView tv_title;
    private final TextView tv_source;
    private final ImageView iv_right_cover_image;
    private final LinearLayout ll_item;

    public InfoStreamLeftTextRightPicHolder(@NonNull View itemView) {
        super(itemView);
        //view
        tv_title=itemView.findViewById(R.id.tv_title);
        iv_right_cover_image=itemView.findViewById(R.id.iv_right_cover_image);
        tv_source=itemView.findViewById(R.id.tv_source);
        ll_item=itemView.findViewById(R.id.ll_item);

    }

    public void setData(InfoBean.DataBean dataBean, Activity acticity) {
        if(dataBean==null){
            return;
        }
        tv_title.setText(dataBean.getTitle());
        tv_source.setText(AppTimeUtils.getInfoPublicTime(dataBean.getPublish_time())+" "+dataBean.getSource());

        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.getInstance().skipInfoDetailsActivity(dataBean,acticity);

            }
        });


        List<InfoBean.DataBean.CoverImageListBean> imageList=dataBean.getCover_image_list();
        if(imageList==null||imageList.size()==0){
            return;
        }
        if(!TextUtils.isEmpty(imageList.get(0).getUrl())){
            ImgUtil.loadImg(iv_right_cover_image,itemView.getContext(),imageList.get(0).getUrl());
        }



    }

}