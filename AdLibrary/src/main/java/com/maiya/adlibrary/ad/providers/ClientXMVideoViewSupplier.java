package com.maiya.adlibrary.ad.providers;

import android.content.Context;

import com.maiya.adlibrary.ad.widget.ClientXMVideoView;
import com.xinmeng.shadow.base.IXMVideoView;
import com.xinmeng.shadow.base.IXMVideoViewSupplier;

public class ClientXMVideoViewSupplier implements IXMVideoViewSupplier {
    @Override
    public IXMVideoView get(Context context) {
        return new ClientXMVideoView(context);
    }
}
