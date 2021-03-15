package com.cniao5.cainiaowo

import com.alibaba.android.arouter.launcher.ARouter
import com.cniao5.common.BaseApplication
import com.cniao5.common.ktx.application
import com.cniao5.mine.moduleMine
import com.cniao5.study.moduleStudy
import com.test.service.assistant.AssistantApp
import com.test.service.moduleService
import login.moduleLogin
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module

class CnApplication : BaseApplication() {

    private val modules = arrayListOf<Module>(
        moduleService,/*moduleHome,*/ moduleLogin, moduleMine, moduleStudy
    )

    override fun initConfig() {
        super.initConfig()

        //添加common模块之外的其他模块，组件的koin的modules
        loadKoinModules(modules)

        //doKit的初始化配置
        AssistantApp.initConfig(application)

        //初始化Arouter框架
        ARouter.init(application)
    }

}