package com.zhangsheng.shunxin.weather.common;

import com.maiya.sdk.httplibrary.http.ICommonParamsProvider;
import com.xm.xmcommon.XMParam;
import com.zhangsheng.shunxin.weather.utils.LocationUtil;

public class SDKCommonParamsProvider implements ICommonParamsProvider {
    @Override
    public String getAppTypeId() {
        return Configure.appTypeId;
    }

    @Override
    public String getAppQid() {
        return XMParam.getAppQid();
    }

    @Override
    public String getCleanAppQid() {
        return XMParam.getCleanAppQid();
    }

    @Override
    public String getAAID() {
        return XMParam.getAAID();
    }

    @Override
    public String getOAID() {
        return XMParam.getOAID();
    }

    @Override
    public String getLongitude() {
        return LocationUtil.INSTANCE.getLocation().getLng();
    }

    @Override
    public String getLatitude() {
        return LocationUtil.INSTANCE.getLocation().getLat();
    }

    @Override
    public String getProvince() {
        return LocationUtil.INSTANCE.getLocation().getProvince();
    }

    @Override
    public String getCity() {
        return LocationUtil.INSTANCE.getLocation().getCity();
    }

    @Override
    public String getDistrict() {
        return LocationUtil.INSTANCE.getLocation().getDistrict();
    }

    @Override
    public String getSrcqid() {
        return XMParam.getSrcqid();
    }

    @Override
    public String getSrcplat() {
        return XMParam.getSrcplat();
    }

    @Override
    public String getRefQidInfo() {
        return XMParam.getRefQidInfo();
    }
}
