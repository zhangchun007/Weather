package com.zhangsheng.shunxin.ad.params

import androidx.annotation.Keep

@Keep
class AdDefaultConfigBean {
    var update:String="20200222"
    var configs:List<Config>?=null


    class Config{
        var type:String="feed"  //feed信息流  video视频 splash开屏
        var pgtype=""
        var jrId=""
        var jrWeight=80
        var gdtId=""
        var gdtWeight=20
    }
}