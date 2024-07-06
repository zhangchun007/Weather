package com.zhangsheng.shunxin.ad.configs

import com.zhangsheng.shunxin.weather.common.UrlConstant

class ClientHostLinks : BaseClientHostLinks() {

    override fun locationInfoUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_GEO_API}/app-fix/adv/areaCode.data" else "${UrlConstant.GEO_API}/app-fix/adv/areaCode.data"
    }
}