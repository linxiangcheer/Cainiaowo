package com.test.service

import com.cniao5.common.network.KtRetrofit
import org.koin.dsl.module

/*
* Service模块相关的koin的module配置
* */
val moduleService = module {

    //host作为外部参数传进来给KtRetrofit进行初始化，得到ktretrofit单例类对象
    single<KtRetrofit> { (host: String) -> KtRetrofit.initConfig(host) }

}