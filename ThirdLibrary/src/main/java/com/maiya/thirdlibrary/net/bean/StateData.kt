package com.maiya.thirdlibrary.net.bean

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/7/15 15:26
 */

class StateData<T> {
    var status: DataStatus? = null
    var data: T? = null
    var code = 0//http 请求的错误的Code
    var msg : String? = null //http 请求错误的信息提示



    fun success(data: T): StateData<T> {
        status = DataStatus.SUCCESS
        this.data = data
        msg = null
        return this
    }

    /**
     *
     *
     * @param msg
     * @param code
     * @return
     */
    fun failure(code: Int, msg: String): StateData<T> {
        status = DataStatus.ERROR
        data = null
        this.code = code
        this.msg = msg
        return this
    }

    /**
     * 基本只需要维护SUCCESS，ERROR。LOADING 也可以省去
     */
    enum class DataStatus {
        SUCCESS, ERROR
    }

}