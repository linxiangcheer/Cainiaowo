package com.test.service.assistant

import android.app.Application
import com.didichuxing.doraemonkit.DoraemonKit


/*
* 配置dokit的工具类
* */
object AssistantApp {

    fun initConfig(application: Application){
        DoraemonKit.install(application)
    }

}