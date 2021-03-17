package com.cniao5.home

import com.cniao5.common.network.KtRetrofit
import com.cniao5.common.utils.getBaseHost
import com.cniao5.home.net.HomeService
import com.cniao5.home.repo.HomeResource
import com.cniao5.home.repo.IHomeResource
import com.cniao5.home.ui.HomeViewModel
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


/*
* 依赖注入管理 Home的module
* */
val moduleHome = module {

    //service retrofit
    //single声明单例对象
    // single {
    //     KtRetrofit.initConfig("http://yapi.54yct.com/mock/24/") //baseurl
    //         .getService(MineService::class.java)
    // }

    single {
        get<KtRetrofit> { parametersOf(getBaseHost()) }.getService(HomeService::class.java)
    }

    //repo IMineResource
    single { HomeResource(get()) } bind IHomeResource::class

    viewModel { HomeViewModel(get()) }

}