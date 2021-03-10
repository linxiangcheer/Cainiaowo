package com.cniao5.common.network.config

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.NetworkUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.lang.reflect.Type


class CnInterceptor : Interceptor {
    companion object {
        private val gson: Gson = GsonBuilder()
        .enableComplexMapKeySerialization()
        .create()
        private val mapType: Type = object : TypeToken<Map<String, Any>>() {}.type
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest: Request = chain.request()
        //附加的公共headers，封装clientInfo，deviceInfo等，也可以在post请求中，自定义封装headers的字段内容
        //注意这里，服务器用于校检的字段，只能是以下的字段内容，可以缺失，但是不能额外添加，因为服务器未做处理
        val attachHeaders: MutableList<Pair<String, String>> = mutableListOf(
            "appid" to NET_CONFIG_APPID,
            "platform" to "android",//如果重复请求，可能会报重复签名错误，vapi平台标记不会
            "timestamp" to System.currentTimeMillis().toString(),

            "brand" to DeviceUtils.getManufacturer(),
            "model" to DeviceUtils.getModel(),
            "uuid" to DeviceUtils.getUniqueDeviceId(),
            "network" to NetworkUtils.getNetworkType().name,
            "system" to DeviceUtils.getSDKVersionName(),

            "version" to AppUtils.getAppVersionName()
        )
        //token仅在有值的时候才传递
        val tokenStr = "ss"
//        val localToken: String!= SPStaticUtils.getString(SP_KEY_USER_TOKEN, tokenStr)
//        if (localToken.isNotEmpty()) {
//            attachHeaders.add("token" to localToken)
//        }
        val signHeaders:MutableList<Pair<String,String>> = mutableListOf<Pair<String,String>>()
        signHeaders.addAll(attachHeaders)
        //get的请求，参数
        if (originRequest.method=="GET") {
            originRequest.url.queryParameterNames.forEach { key ->
                signHeaders.add(key to (originRequest.url.queryParameter(key) ?: ""))
            }
        }
        //post的请求 formBody形式，或json形式的，都需要将内部的字段，遍历出来，参与sign的计算
        val requestBody: RequestBody? = originRequest.body
        if (originRequest.method == "POST") {
            //formBody
            if (requestBody is FormBody) {
                for (i in 0 until requestBody.size) {
                    signHeaders.add(requestBody.name(i) to requestBody.value(i))
                }
            }
            //json 的body 需要将requestBody反序列化为json转为map application/json
            if (requestBody?.contentType()?.type == "application" && requestBody.contentType()?.subtype == "json") {
                kotlin.runCatching {
                    val buffer = okio.Buffer()
                    requestBody.writeTo(buffer)
                    buffer.readByteString().utf8()
                }.onSuccess {
                    val map: Map<String, Any> = gson.fromJson<Map<String, Any>>(it, mapType)
                    map.forEach { entry ->
                        // value 目前json单层级
                        signHeaders.add(entry.key to entry.value.toString())
                    }
                }
            }
        }
        //算法：都必须是非空参数！！！ sign=MD5(ascii排序后的headers及params的key=value拼接&后，最后拼接
        // appkey和value) //32位大写
        val signValue: String = attachHeaders
            .sortedBy { it.first }
            .joinToString("&") { "${it.first}=${it.second}" }
            .plus("&appkey=$ENT_CONFIG_APPKEY")
        val newBuilder: Request.Builder = originRequest.newBuilder()
            .cacheControl(CacheControl.FORCE_CACHE)
        attachHeaders.forEach { newBuilder.header(it.first, it.second) }
        newBuilder.header("sign", EncryptUtils.encryptMD5ToString(signValue))

        if (originRequest.method == "POST" && requestBody != null) {
            newBuilder.post(requestBody)
        } else if (originRequest.method == "GET") {
            newBuilder.get()
        }
        return chain.proceed(newBuilder.build())
    }
}