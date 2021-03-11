package com.cniao5.common.network

import com.cniao5.common.network.config.CnInterceptor
import com.cniao5.common.network.support.LiveDataCallAdapterFactory
import com.cniao5.common.utils.HostInterceptor
import com.zsk.common.network.config.KtHttpLogInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
*封装的retrofit请求类
 */
object KtRetrofit {

    private val mOkClient:OkHttpClient=OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS) //完整请求超时时长，从发起到接收返回数据，默认值0，不限定
        .connectTimeout(10, TimeUnit.SECONDS) //与服务器建立连接的时长，默认10s
        .readTimeout(10, TimeUnit.SECONDS) //读取服务器返回数据的时长
        .writeTimeout(10, TimeUnit.SECONDS) //向服务器写入数据的时长，默认10s
        .retryOnConnectionFailure(true) //重连
        .followRedirects(false)//重定向
//        .cookieJar(LocalCookieJar())
        .addInterceptor(HostInterceptor()) //用于替换baseHost的拦截器
        .addNetworkInterceptor(CnInterceptor()) //公共header的拦截器
        .addNetworkInterceptor(KtHttpLogInterceptor {
            logLevel(KtHttpLogInterceptor.LogLevel.BODY)
        })//添加网络拦截器，可以对okHttp的网络请求做拦截处理，不同于应用拦截器，这里能感知所以网络状态，比如重定向
        .build()

    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .client(mOkClient)

    private var retrofit:Retrofit?=null //retrofit 请求client
    /**
     * 初始化配置
     */
    fun initConfig(baseUrl:String,okClient:OkHttpClient= mOkClient): KtRetrofit {
        retrofit = retrofitBuilder.baseUrl(baseUrl).client(mOkClient).build()
        return this
    }

    /**
     * 获取retrofit的service对象
     */
    fun <T> getService(serviceClazz:Class<T>):T{
        if (retrofit ==null){
            throw UninitializedPropertyAccessException("Retrofit必须初始化，需要配置baseUrl")
        }else{
            return retrofit!!.create(serviceClazz)
        }
    }
}