package com.zhangsheng.shunxin.weather.net


import com.google.gson.GsonBuilder
import com.maiya.thirdlibrary.ext.nN
import com.zhangsheng.shunxin.weather.common.Configure
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.UrlConstant
import com.zhangsheng.shunxin.weather.net.convert.JsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


/**
 * 作者：全荣浩 on 2018/3/27 16:38
 * 邮箱：672114236@qq.com
 */
object RetrofitFactory {
    private var retrofit: Retrofit? = null
    var BASE_URL = if (Configure.Debug) UrlConstant.DEBUG_APP_NATIVE else UrlConstant.APP_NATIVE

    private fun getApiService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .addConverterFactory(JsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
            hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            readTimeout(8, TimeUnit.SECONDS)
            callTimeout(8, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            addInterceptor(CommonInterceptor())
            if (Configure.Debug) {
                addInterceptor(interceptor)
            }
        }.build()

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


    fun <T> create(service: Class<T>): T {
        return getApiService().create(service)
    }

    fun create(): Api {
        if (retrofit == null) {
            retrofit = getApiService()
        }
        return retrofit.nN().create(Api::class.java)
    }
}