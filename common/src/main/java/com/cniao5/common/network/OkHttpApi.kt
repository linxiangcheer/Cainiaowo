package com.cniao5.common.network

import androidx.collection.SimpleArrayMap
import com.cniao5.common.network.config.CniaoInterceptor
import com.cniao5.common.network.config.LocalCookieJar
import com.cniao5.common.network.config.RetryInterceptor
import com.cniao5.common.network.support.IHttpCallback
import com.google.gson.Gson
import com.zsk.common.network.config.KtHttpLogInterceptor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *
 */
class OkHttpApi private constructor() : HttpApi {

    var maxRetry = 0//最大重试 次数

    //存储请求，用于取消
    private val callMap = SimpleArrayMap<Any, Call>()

    //okHttpClient
    private val defaultClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)//完整请求超时时长，从发起到接收返回数据，默认值0，不限定,
        .connectTimeout(10, TimeUnit.SECONDS)//与服务器建立连接的时长，默认10s
        .readTimeout(10, TimeUnit.SECONDS)//读取服务器返回数据的时长
        .writeTimeout(10, TimeUnit.SECONDS)//向服务器写入数据的时长，默认10s
        .retryOnConnectionFailure(true)//重连
        .followRedirects(false)//重定向
        .cache(Cache(File("sdcard/cache", "okhttp"), 1024))
//        .cookieJar(CookieJar.NO_COOKIES)
        .cookieJar(LocalCookieJar())
        .addNetworkInterceptor(CniaoInterceptor())//公共header的拦截器
        .addNetworkInterceptor(KtHttpLogInterceptor {
            logLevel(KtHttpLogInterceptor.LogLevel.BODY)
        })//添加网络拦截器，可以对okHttp的网络请求做拦截处理，不同于应用拦截器，这里能感知所有网络状态，比如重定向。
        .addNetworkInterceptor(RetryInterceptor(maxRetry))
//        .hostnameVerifier(HostnameVerifier { p0, p1 -> true })
//        .sslSocketFactory(sslSocketFactory = null,trustManager = null)
        .build()

    private var mClient = defaultClient

    fun getClient() = mClient

    /**
     * 配置自定义的client
     */
    fun initConfig(client: OkHttpClient) {
        this.mClient = client
    }

    companion object {
        @Volatile
        private var api: OkHttpApi? = null

        @Synchronized
        fun getInstance(): OkHttpApi {
            return api ?: OkHttpApi().also { api = it }
        }
    }


    override fun get(params: Map<String, Any>, urlStr: String, callback: IHttpCallback) {
        val urlBuilder = urlStr.toHttpUrl().newBuilder()
        params.forEach { entry ->
            urlBuilder.addEncodedQueryParameter(entry.key, entry.value.toString())
        }

        val request = Request.Builder()
            .get()
            .tag(params)
            .url(urlBuilder.build())
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()
        val newCall = mClient.newCall(request)
        //存储请求，用于取消
        callMap.put(request.tag(), newCall)
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailed(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(response.body?.string())
            }

        })
    }

    override fun post(body: Any, urlStr: String, callback: IHttpCallback) {

        val reqBody = Gson().toJson(body).toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .post(reqBody)
            .url(urlStr)
            .tag(body)
            .build()

        val newCall = mClient.newCall(request)
        //存储请求，用于取消
        callMap.put(request.tag(), newCall)
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailed(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(response.body?.string())
            }

        })
    }


    /**
     * 取消网络请求，tag就是每次请求的id 标记，也就是请求的传参
     */
    override fun cancelRequest(tag: Any) {
        callMap.get(tag)?.cancel()
    }


    /**
     * 取消所有网络请求
     */
    override fun cancelAllRequest() {
        for (i in 0 until callMap.size()) {
            callMap.get(callMap.keyAt(i))?.cancel()
        }
    }

    //使用协程形式的 get请求，使用runBlocking，也可以使用suspend修饰
    fun get(params: Map<String, Any>, urlStr: String) = runBlocking {
        val urlBuilder = urlStr.toHttpUrl().newBuilder()
        params.forEach { entry ->
            urlBuilder.addEncodedQueryParameter(entry.key, entry.value.toString())
        }

        val request = Request.Builder()
            .get()
            .tag(params)
            .url(urlBuilder.build())
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()
        val newCall = mClient.newCall(request)
        //存储请求，用于取消
        callMap.put(request.tag(), newCall)
        newCall.call()
    }

    /**
     * 自定义扩展函数，扩展okHttp的Call的异步执行方式，结合coroutines，返回DataResult的数据响应
     * 也可以使用resumeWith返回的是Result<T>
     * [async] 默认是异步调用，可选参数，false的话就是同步调用
     */
    private suspend fun Call.call(async: Boolean = true): Response {
        return suspendCancellableCoroutine { continuation ->
            if (async) {
                enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        //避免不必要的冗余调用
                        if (continuation.isCancelled) return
                        continuation.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        continuation.resume(response)
                    }
                })
            } else {
                continuation.resume(execute())
            }
            continuation.invokeOnCancellation {
                try {
                    cancel()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

}