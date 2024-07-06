package com.maiya.thirdlibrary.base


import androidx.lifecycle.*
import com.maiya.thirdlibrary.ext.LogE
import com.maiya.thirdlibrary.ext.nN
import com.maiya.baselibrary.net.NetException
import com.maiya.thirdlibrary.net.bean.ActionBean
import com.maiya.thirdlibrary.net.bean.BaseResponse
import com.maiya.thirdlibrary.net.bean.NetStatus
import com.maiya.thirdlibrary.net.callback.CallResult
import kotlinx.coroutines.*
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.Exception


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/3 10:26
 */
open class BaseViewModel : ViewModel() {

    val pageAction: MutableLiveData<ActionBean> = MutableLiveData()

    protected fun <M> callNativeApi(
        func: suspend () -> BaseResponse<M>,
        callBack: CallResult<M>? = null
    ) {
        viewModelScope.async(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    var data = func()
                    withContext(Dispatchers.Main) {
                        when {
                            data.ret != 200 -> callBack?.failed(data.ret, data.msg)
                            data.nN().data.nN().code != 0 -> callBack?.failed(
                                data.nN().data.nN().code,
                                data.nN().data.nN().msg
                            )
                            else -> {
                                callBack?.ok(data.data.nN().data)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                LogE("数据异常：$e  ${e.printStackTrace()}")
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is SocketTimeoutException,
                    is HttpException -> {
                        callBack?.failed(NetStatus.netError)
                    }
                    is NetException -> {
                        callBack?.failed(NetStatus.netError)
                    }
                    is JSONException -> callBack?.failed(NetStatus.netError)
                    else -> {
                        callBack?.failed(NetStatus.fail)
                    }
                }
            }
        }
    }


    protected fun <M> callApi(
        func: suspend () -> M,
        callBack: CallResult<M>? = null
    ) {
        viewModelScope.async(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    var data = func()
                    withContext(Dispatchers.Main) {
                        callBack?.ok(data)
                    }
                }
            } catch (e: Exception) {
                LogE("数据异常：$e  ${e.printStackTrace()}")
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is SocketTimeoutException,
                    is IOException,
                    is HttpException -> {
                        callBack?.failed(NetStatus.netError)
                    }
                    is NetException -> {
                        LogE("自定义异常：${e.errorCode}  ${e.errorMsg}")
                        callBack?.failed(NetStatus.netError, e.errorMsg)
                    }
                    is JSONException -> callBack?.failed(NetStatus.netError)
                    else -> {
                        callBack?.failed(NetStatus.fail)
                    }
                }
            }
        }
    }
    fun loadSuccess() {
        pageAction.postValue(ActionBean().apply {
            this.code = NetStatus.success
        })
    }

    fun showLoading() {
        pageAction.postValue(ActionBean().apply {
            this.code = NetStatus.showLoading
        })
    }

    fun loadFail(code: Int, msg: String) {
        pageAction.postValue(ActionBean().apply {
            this.code = code
            this.msg = msg
        })
    }


}