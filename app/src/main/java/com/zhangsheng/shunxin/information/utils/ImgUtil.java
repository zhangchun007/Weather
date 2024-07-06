package com.zhangsheng.shunxin.information.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.constant.Constants;
import com.necer.utils.DensityUtil;

/**
 * @Author:liupengbing
 * @Data: 2020/6/2 7:43 PM
 * @Email:aliupengbing@163.com
 */
public class ImgUtil {

    private static RequestOptions requestOptions;

    public static  void  loadImg(ImageView imageView, Context context, String url){
        if(requestOptions==null) {
            requestOptions = new RequestOptions().transforms(new CenterCrop(),
                    new RoundedCorners(DensityUtil.dp2px(Constants.infoStreamCorner)))
            .placeholder(R.mipmap.icon_info_default)
                    .error(R.mipmap.icon_info_default)
                    .fallback(R.mipmap.icon_info_default);
        }
        Glide.with(context).load(url)
                .format(DecodeFormat.PREFER_RGB_565)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(requestOptions)
                .thumbnail(0.1f)
                .into(imageView);
    }

}
