package com.maiya.adlibrary.ad.csj;

import com.bykv.vk.openvk.TTVfManager;
import com.xinmeng.shadow.branch.source.csj.ICSJManagerProvider;

public class CSJManagerProvider implements ICSJManagerProvider {
    @Override
    public TTVfManager provide() {
        return CSJAdManagerHolder.getInstance();
    }
}
