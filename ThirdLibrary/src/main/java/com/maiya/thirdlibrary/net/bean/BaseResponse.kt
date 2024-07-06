package com.maiya.thirdlibrary.net.bean

class BaseResponse<T>  {


    var ret: Int = 0
    var data: DataBean<T>? = null
    var msg: String = ""

    class DataBean<T> {

        var code: Int = -1   //0 成功
        var msg: String = ""
        var data: T?=null

    }
}