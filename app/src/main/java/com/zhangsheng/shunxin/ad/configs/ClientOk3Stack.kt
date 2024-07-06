package com.zhangsheng.shunxin.ad.configs

import android.text.TextUtils
import com.mooc.network.core.Header
import com.mooc.network.core.HttpResponse
import com.mooc.network.core.Request
import com.xinmeng.shadow.base.IHttpStack
import com.xinmeng.shadow.base.VAdError
import com.zhangsheng.shunxin.ad.GzipRequestInterceptor
import com.zhangsheng.shunxin.weather.net.SSLSocketClient
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * okhttp3网络适配
 */
class ClientOk3Stack
/**
 * 默认构造函数
 */ @JvmOverloads constructor(private val mClient: OkHttpClient = OkHttpClient()) :
    IHttpStack {
    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    @Throws(IOException::class, VAdError::class)
    override fun performRequest(
        request: Request<*>,
        additionalHeaders: Map<String, String>
    ): HttpResponse {
        val timeoutMs = request.timeoutMs
        val client = mClient.newBuilder()
            .readTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
//            .addInterceptor(provideLoggingInterceptor())
            .addInterceptor(GzipRequestInterceptor())
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .build()
        val okHttpRequestBuilder = okhttp3.Request.Builder()
        okHttpRequestBuilder.url(request.url)

        //处理内部header
        if (request.headers != null) {
            for ((k, v) in request.headers) {
                if (!TextUtils.isEmpty(k) && !TextUtils.isEmpty(v)) {
                    okHttpRequestBuilder.addHeader(k, v)
                }
            }
        }

        //处理额外header
        if (additionalHeaders != null) {
            for ((k, v) in additionalHeaders) {
                if (!TextUtils.isEmpty(k) && !TextUtils.isEmpty(v)) {
                    okHttpRequestBuilder.addHeader(k, v)
                }
            }
        }
        setConnectionParametersForRequest(
            okHttpRequestBuilder,
            request
        )
        val okHttpRequest = okHttpRequestBuilder.build()
        val okHttpCall = client.newCall(okHttpRequest)
        val okHttpResponse = okHttpCall.execute()
        return responseFromConnection(okHttpResponse)
    }

    @Throws(IOException::class)
    private fun responseFromConnection(okHttpResponse: Response): HttpResponse {
        val code = okHttpResponse.code()
        if (code == -1) {
            throw IOException("response code error from okhttp.")
        }
        val length = java.lang.Long.valueOf(okHttpResponse.body()!!.contentLength()).toInt()
        val okHeaders = okHttpResponse.headers()
        val headers: MutableList<Header?> =
            ArrayList()
        if (okHeaders != null) {
            var i = 0
            val len = okHeaders.size()
            while (i < len) {
                val name = okHeaders.name(i)
                val value = okHeaders.value(i)
                if (name != null) {
                    headers.add(Header(name, value))
                }
                i++
            }
        }

        //构造网盟HttpResponse
        return HttpResponse(
            code,
            headers,
            length,
            okHttpResponse.body()!!.byteStream()
        )
    }

    companion object {
        @Throws(IOException::class)
        private fun setConnectionParametersForRequest(
            builder: okhttp3.Request.Builder,
            request: Request<*>
        ) {
            when (request.method) {
                Request.METHOD_GET, Request.METHOD_DEPRECATED_GET_OR_POST -> builder.get()
                Request.METHOD_DELETE -> builder.delete()
                Request.METHOD_POST -> builder.post(createRequestBody(request))
                Request.METHOD_PUT -> builder.put(createRequestBody(request))
                Request.METHOD_HEAD -> builder.head()
                Request.METHOD_OPTIONS -> builder.method("OPTIONS", null)
                Request.METHOD_TRACE -> builder.method("TRACE", null)
                Request.METHOD_PATCH -> builder.patch(createRequestBody(request))
                else -> throw IllegalStateException("Unknown method type.")
            }
        }

        private fun createRequestBody(request: Request<*>): RequestBody? {
            var body: ByteArray? = null
            try {
                body = request.body
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return if (body == null) null else RequestBody.create(
                MediaType.parse(request.bodyContentType),
                body
            )
        }
    }
    /**
     * 可以替换成自己的okClient
     *
     * @param mClient okhttp client
     */
}