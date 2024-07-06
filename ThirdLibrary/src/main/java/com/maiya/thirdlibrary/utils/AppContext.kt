package com.maiya.thirdlibrary.utils

import android.app.Application
import android.content.Context
import com.maiya.thirdlibrary.ext.nN

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/13 13:48
 */

class AppContext{
    companion object{
        var context:Application?=null

        fun getContext(): Context{
            if (context ==null){
                context = AppUtils.getApplicationByReflect()
            }
            return context.nN()
        }
    }
}