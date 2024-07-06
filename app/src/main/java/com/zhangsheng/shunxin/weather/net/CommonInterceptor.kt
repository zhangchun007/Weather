package com.zhangsheng.shunxin.weather.net


import android.util.ArrayMap
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.ext.Base64
import com.zhangsheng.shunxin.weather.ext.encrypt
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.net.bean.Params
import com.xm.xmcommon.XMParam
import com.zhangsheng.shunxin.weather.common.UrlConstant
import okhttp3.*
import okio.Buffer
import org.json.JSONObject
import java.io.IOException
import kotlin.jvm.Throws


class CommonInterceptor : Interceptor {
    companion object {
        val DOMAIN = "domain"
    }

    @Synchronized
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        var domainName = obtainDomainNameFromHeaders(request).isStr("")
        val reBuildRequest = rebuildRequest(request, domainName)
        return chain.proceed(reBuildRequest)
    }


    @Throws(IOException::class)
    private fun rebuildRequest(request: Request, domainName: String): Request {
        var newRequest = when {
            "POST" == request.method() -> rebuildPostRequest(request, domainName)
            "GET" == request.method() -> rebuildGetRequest(request, domainName)
            else -> request
        }
        return newRequest
    }

    private var commonParams: HashMap<String, String>? = null

    /**
     * 对post请求添加统一参数
     */

    private fun rebuildPostRequest(request: Request, domainName: String): Request {

        val originalRequestBody = request.body()!!
        var newRequestBody: RequestBody? = null
        var url = rebuildUrl(request, domainName)

        if (originalRequestBody is FormBody) { // 传统表单

            val builder = FormBody.Builder()
            val requestBody = request.body() as FormBody
            val fieldSize = requestBody.size()
            this.commonParams = HashMap()
            var paramsJson = JsonObject()

            for (i in 0 until fieldSize) {
                if (requestBody.value(i).isNotEmpty()) {
                    paramsJson.addProperty(requestBody.name(i), requestBody.value(i))
                    commonParams.nN()[requestBody.name(i)] = requestBody.value(i)
                }
            }
            paramsJson.addProperty("cust_client_time", "${System.currentTimeMillis() / 1000}")
            when (domainName) {
                UrlConstant.NATIVE -> {
                    var dataJson = JsonObject()
                    var map = ArrayMap<String, String>()
                    Try {
                        map.putAll(XMParam.getAppCommonParamMap())
                        map.putAll(XMParam.getSecondAppCommonParamMap())
                    }
                    dataJson.addProperty("pub", Gson().toJson(map).Base64())
                    dataJson.add("params", paramsJson)
                    var nativeParams = HashMap<String, String>()
                    nativeParams.nN()["data"] = ReCodeUtils.httpParmsEncode("$dataJson")
                    nativeParams.nN()["ts"] = "${System.currentTimeMillis()}"
                    nativeParams.nN()["service"] =
                        request.url().toString().split("service=").listIndex(1)
                    var paramsList = ArrayList<Params>()
                    nativeParams.nN().remove("")
                    nativeParams.nN().toSortedMap()
                        .forEach { paramsList.add(Params(it.key, it.value)) }
                    var buffer = StringBuffer()
                    for (i in paramsList.size - 1 downTo 0) {
                        builder.add(paramsList.listIndex(i).key, paramsList.listIndex(i).value)
                        if (i == 0) {
                            buffer.append("${paramsList.listIndex(i).key}=${paramsList.listIndex(i).value}")
                        } else {
                            buffer.append("${paramsList.listIndex(i).key}=${paramsList.listIndex(i).value}@@")
                        }
                    }
                    builder.add("sign", buffer.toString().encrypt())
                    newRequestBody = builder.build()
                }
                UrlConstant.CONTROL, UrlConstant.WIDGET -> {
                    var map = ArrayMap<String, String>()
                    map.putAll(XMParam.getAppCommonParamMap())
                    map.putAll(XMParam.getSecondAppCommonParamMap())
                    commonParams?.let {
                        commonParams?.remove("")
                        map.putAll(commonParams.nN())
                    }
                    var json = JSONObject(Gson().toJson(map))
                    builder.add("rOSwHu", ReCodeUtils.encode(json.toString(), 0))
                    newRequestBody = builder.build()
                }
                else -> {
                    commonParams.nN().forEach {
                        builder.add(it.key, it.value)
                    }
                    newRequestBody = builder.build()
                }
            }


        } else {
            try {
                val jsonObject: JSONObject = if (originalRequestBody.contentLength() == 0L) {
                    JSONObject()
                } else {
                    JSONObject(getParamContent(originalRequestBody))
                }

                newRequestBody =
                    RequestBody.create(originalRequestBody.contentType(), jsonObject.toString())

            } catch (e: Exception) {
                newRequestBody = originalRequestBody
                e.printStackTrace()
            }

        }

        url?.let {
            return request.newBuilder().url(url).method(request.method(), newRequestBody)
                .build()
        }

        return request.newBuilder().method(request.method(), newRequestBody)
            .build()
    }


    /**
     * 获取常规post请求参数
     */
    @Throws(IOException::class)
    private fun getParamContent(body: RequestBody): String {
        val buffer = Buffer()
        body.writeTo(buffer)
        return buffer.readUtf8()
    }

    /**
     * 对get请求做统一参数处理
     */
    private fun rebuildGetRequest(request: Request, domainName: String): Request {

        return request

//        if (commonParams == null || commonParams!!.size == 0) {
//            return request
//        }
//        val url = request.url.toString()
//        val separatorIndex = url.lastIndexOf("?")
//        val sb = StringBuilder(url)
//        if (separatorIndex == -1) {
//            sb.append("?")
//        }
//        for (commonParamKey in commonParams!!.keys) {
//            sb.append("&").append(commonParamKey).append("=").append(commonParams!![commonParamKey])
//        }
//        val requestBuilder = request.newBuilder()
//        return requestBuilder.url(sb.toString()).build()
    }


    private fun obtainDomainNameFromHeaders(request: Request): String? {

        val headers = request.headers(DOMAIN)
        if (headers.size == 0) return null
        require(headers.size <= 1) { "Only one Domain in the headers" }
        return request.header(DOMAIN)
    }

    private fun rebuildUrl(request: Request, domainName: String): HttpUrl? {
        if (domainName.isEmpty()) return null
        var oldUrl = request.url()
        var newUrl = getAppModel().findUrl(domainName)
        return if (newUrl.isNullOrEmpty()) oldUrl else HttpUrl.parse(
            oldUrl.toString().replace(
                getAppModel().findUrl(UrlConstant.NATIVE).nN(), newUrl
            )
        )
    }

//    @Throws(IOException::class)
//    private fun rebuildResponse(response: Response, domainName: String): Response {
//
//        return when (domainName) {
//            Constant.NATIVE -> {
//                var response = response
//                val body = response.body()
//                val source = body.nN().source()
//                source.request(Long.MAX_VALUE) // Buffer the entire body.
//                val buffer = source.buffer
//                var charset: Charset = Charset.defaultCharset()
//                val contentType = body.nN().contentType()
//                if (contentType != null) {
//                    charset = contentType.charset(charset).nN()
//                }
//                val string = buffer.clone().readString(charset)
//                val bodyString: String? = decryptNative(string,response.request().url().toString()).isStr("{}")
//                val responseBody = ResponseBody.create(contentType,bodyString)
//                response.newBuilder().body(responseBody).build()
//            }
//            else -> response
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun decryptNative(data: String?, url: String): String? {
//        try {
//            if (data.nN().isNotEmpty()) {
//                var json =JSONObject(data)
//                var data = ReCodeUtils.httpResponseDecode(json.getString("data"))
//                var responseBean = Gson().fromJson<BaseResponse>(data, BaseResponse::class.java)
//                when {
//                    responseBean.ret != 200 -> throw NetException(
//                        responseBean.ret,
//                        responseBean.msg
//                    )
//                    responseBean.nN().data.nN().code != 0 -> throw NetException(
//                        responseBean.nN().data.nN().code,
//                        responseBean.nN().data.nN().msg
//                    )
//                    else ->{
//                        LogE("parse->${url}:${responseBean.data.nN().data}")
//                        return "${responseBean.data.nN().data}"
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            throw IOException()
//        }
//        return data.nN()
//    }


//
}
