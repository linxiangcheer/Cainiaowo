package com.cniao5.cainiaowo

import com.cniao5.common.BaseApplication
import com.cniao5.common.ktx.application
import com.test.service.assistant.AssistantApp

class CnApplication : BaseApplication() {

    override fun initConfig() {
        super.initConfig()
        //doKit的初始化配置
        AssistantApp.initConfig(application)
    }

}