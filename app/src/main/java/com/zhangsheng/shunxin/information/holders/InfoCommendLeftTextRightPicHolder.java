package com.zhangsheng.shunxin.information.holders;

import android.app.Activity;
import android.content.Intent;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.InfoDetails.InfoDetailsActivity;
import com.zhangsheng.shunxin.information.bean.InfoCommendBean;
import com.zhangsheng.shunxin.information.utils.ImgUtil;
import com.zhangsheng.shunxin.information.utils.JrttPostBackUtils;
import com.zhangsheng.shunxin.weather.ext.AppExtKt;
import com.zhangsheng.shunxin.information.bean.InfoCommendBean;
import com.zhangsheng.shunxin.information.utils.ImgUtil;
import com.zhangsheng.shunxin.information.utils.JrttPostBackUtils;

import java.util.List;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 1:29 PM
 * @Email:aliupengbing@163.com
 */
public class InfoCommendLeftTextRightPicHolder extends RecyclerView.ViewHolder {
    private final TextView tv_title;
    private final TextView tv_source;
    private final ImageView iv_right_cover_image;
    private final LinearLayout ll_item;

    private int curPos=0;

    public InfoCommendLeftTextRightPicHolder(@NonNull View itemView) {
        super(itemView);
        //view
        tv_title=itemView.findViewById(R.id.tv_title);
        iv_right_cover_image=itemView.findViewById(R.id.iv_right_cover_image);
        tv_source=itemView.findViewById(R.id.tv_source);
        ll_item=itemView.findViewById(R.id.ll_item);

    }

    public void setData(InfoCommendBean.DataBean dataBean, Activity acticity, int position) {
        if(dataBean==null){
            return;
        }
        if(position>2){
            curPos=position-1;
        }else{
            curPos=position;
        }
        tv_title.setText(dataBean.getTitle());
        tv_source.setText(dataBean.getSource());
        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=dataBean.getArticle_url()+"&hide_comments=1&hide_relate_news=1";
                Intent intent=new Intent(acticity, InfoDetailsActivity.class);
                intent.putExtra("Article_url",url);
                intent.putExtra("item_id",String.valueOf(dataBean.getItem_id()));
                intent.putExtra("group_id",String.valueOf(dataBean.getGroup_id()));
                intent.putExtra("has_video",dataBean.isHas_video());
                acticity.startActivityForResult(intent,200);
//                //详情页推荐新闻 点击上报
                JrttPostBackUtils.getInstance().clickPostBack(dataBean);
//                //点击详情页推荐新闻 上报详情页停留时间
//                InfoDetailsActivity.infoDetilsStayTimePostBack();

                InfoDetailsActivity.jrttPostBack(curPos);

            }
        });

        List<InfoCommendBean.DataBean.LargeImageListBean> imageList=dataBean.getLarge_image_list();
        if(imageList==null||imageList.size()==0){
            iv_right_cover_image.setImageResource(R.mipmap.icon_info_default);
            return;
        }
        if(!TextUtils.isEmpty(imageList.get(0).getUrl())){
            ImgUtil.loadImg(iv_right_cover_image,itemView.getContext(),imageList.get(0).getUrl());

        }else{
            iv_right_cover_image.setImageResource(R.mipmap.icon_info_default);
        }
    }


}
