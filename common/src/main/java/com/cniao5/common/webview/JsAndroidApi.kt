package com.cniao5.common.webview

import android.webkit.JavascriptInterface
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.cniao5.common.network.config.SP_KEY_USER_TOKEN
import com.cniao5.common.utils.MySpUtils.getString


/*
 *
 */
object JsAndroidApi {

    const val JS_CALL_APP_KEY = "cniaoApp"

    @JavascriptInterface
    fun getAppToken(): String {
        LogUtils.w("JsAndroidApi 中 js调用了getToken")
        return "11"
    }
}