package com.maiya.baselibrary.net

import java.io.IOException

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/17 19:13
 */
class  NetException constructor(code:Int,msg:String)  : IOException() {
    var errorCode=code

    var errorMsg=msg
}