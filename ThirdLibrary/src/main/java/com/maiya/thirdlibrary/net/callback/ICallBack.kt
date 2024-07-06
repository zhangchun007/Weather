package com.maiya.baselibrary.net.callback



interface ICallBack<M> {
    fun ok(result: M?=null)

    fun failed(code:Int=0,msg:String="")
}