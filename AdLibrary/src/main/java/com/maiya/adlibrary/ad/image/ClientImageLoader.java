package com.maiya.adlibrary.ad.image;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.maiya.adlibrary.R;
import com.xinmeng.shadow.base.IImageLoader;

public class ClientImageLoader implements IImageLoader {
    private static final String GIF = ".gif";
    private static String url;

    public static DiskCacheStrategy getDiskCacheStrategy(String url) {
        DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.ALL;
        if (url != null && url.endsWith(GIF)) {
            diskCacheStrategy = DiskCacheStrategy.RESOURCE;
        }
        return diskCacheStrategy;
    }

    @Override
    public void loadImage(Context context, ImageView iv, String url) {
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .placeholder(R.drawable.adv_default_bg)
                .error(R.drawable.adv_default_bg)
                .diskCacheStrategy(diskCacheStrategy)
                .into(iv);
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadImage(Context context, String url, final Callback callback) {
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .asDrawable()
                .load(url)
                .diskCacheStrategy(diskCacheStrategy)
                .priority(Priority.IMMEDIATE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callback != null && context != null) {
                            if (context instanceof Activity) {
                                if (!((Activity) context).isFinishing()) {
                                    callback.onException(e);
                                }
                            } else {
                                callback.onException(e);
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (callback != null && context != null) {
                            if (context instanceof Activity) {
                                if (!((Activity) context).isFinishing()) {
                                    callback.onResourceReady(resource);
                                }
                            } else {
                                callback.onResourceReady(resource);
                            }
                        }
                        return false;
                    }
                }).preload();
    }

    @Override
    public void loadImageRounded(Context context, ImageView imageView, String url, int radius) {
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

}
