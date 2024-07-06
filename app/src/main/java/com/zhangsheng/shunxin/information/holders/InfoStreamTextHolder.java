package com.zhangsheng.shunxin.information.holders;

import android.app.Activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.utils.ActivityUtil;
import com.zhangsheng.shunxin.information.utils.AppTimeUtils;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.utils.ActivityUtil;
import com.zhangsheng.shunxin.information.utils.AppTimeUtils;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 1:29 PM
 * @Email:aliupengbing@163.com
 */
public class InfoStreamTextHolder extends RecyclerView.ViewHolder {
    private final TextView tv_title;
    private final LinearLayout ll_item;
    private final TextView tv_source;


    public InfoStreamTextHolder(@NonNull View itemView) {
        super(itemView);
        //view
        tv_title=itemView.findViewById(R.id.tv_title);
        ll_item=itemView.findViewById(R.id.ll_item);
        tv_source=itemView.findViewById(R.id.tv_source);

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
    }

}
