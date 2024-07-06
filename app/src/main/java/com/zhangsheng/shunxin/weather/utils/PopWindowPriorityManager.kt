package com.zhangsheng.shunxin.weather.utils

import androidx.annotation.Keep
import com.maiya.thirdlibrary.ext.nN
import kotlinx.coroutines.*
import java.util.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/12/17 14:49
 */
object PopWindowPriorityManager {
    val job by lazy { CoroutineScope(Dispatchers.Main + SupervisorJob()) }
    private var currentFun: Function? = null // 当前任务

    /**
     * 事件优先级
     */
    private var managers: HashMap<Int, Function?> = HashMap()


    fun pushTask(priority: Int, function: () -> Any) {
        job.launch {
            if (currentFun == null) {
                currentFun = Function(function, priority)
                function.invoke()
            } else {
                managers[priority] = Function(function, priority)
            }
        }
    }

    fun checkTask() {
        if (currentFun == null) return
        job.launch {
            managers[currentFun.nN().key] = null
            currentFun = null
            run breaking@{
                for (i in 0 until managers.size) {
                    if (managers[i] != null) {
                        currentFun = managers[i].nN()
                        currentFun.nN().function.invoke()
                        return@breaking
                    }
                }
            }
        }
    }

    fun clear() {
        managers.clear()
        job.cancel()
    }


    @Keep
    data class Function(var function: () -> Any = {}, var key: Int)
}