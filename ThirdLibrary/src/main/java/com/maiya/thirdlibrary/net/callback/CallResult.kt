package com.maiya.thirdlibrary.net.callback

import com.maiya.baselibrary.net.callback.ICallBack


open class CallResult<M>() : ICallBack<M> {
    override fun failed(code:Int,msg:String) {

    }

    override fun ok(result: M?) {


    }

}