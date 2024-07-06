package com.zhangsheng.shunxin.weather.net.bean

class PushBean{
    var code:String="-1"
    var tags:List<String>?=null
    var msg:String=""
    override fun toString(): String {
        return "PushBean(code='$code', tags=$tags, msg='$msg')"
    }


}
