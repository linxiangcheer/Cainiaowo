package com.cniao5.common.ktx

import android.app.Application

/*
* Application相关的ktx扩展
* */

    /*
    * Application的扩展字段 参数一致性
    * */
val Application.application: Application
    get() = this