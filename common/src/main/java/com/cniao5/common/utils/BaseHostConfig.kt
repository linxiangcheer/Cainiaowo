package com.cniao5.common.utils

import com.cniao5.common.BuildConfig
import com.cniao5.common.network.config.SP_KEY_BASE_HOST

/*
* 基础baseUrl的配置，可用于dokit的serverHost
* */
fun getBaseHost(): String {
    return if (BuildConfig.DEBUG) { //如果从缓存中读取为空的话就是正式的host配置
        MySpUtils.getString(SP_KEY_BASE_HOST) ?: HOST_PRODUCT
    } else {
        HOST_PRODUCT
    }
}

/*
* 更新配置host
* */
fun setBaseHost(host: String) {
    MySpUtils.put(SP_KEY_BASE_HOST, host)
}

//不同的baseHost
const val HOST_DEV = "http://yapi.54yct.com/mock/24/2" //开发环境下的host配置
const val HOST_QA = "http://yapi.54yct.com/mock/24/1" //QA环境下的host配置
const val HOST_PRODUCT = "http://yapi.54yct.com/mock/24/" //正式环境下的host配置(默认)