package com.cniao5.mine

import com.cniao5.common.network.KtRetrofit
import com.cniao5.common.utils.getBaseHost
import com.cniao5.mine.net.MineService
import com.cniao5.mine.repo.IMineResource
import com.cniao5.mine.repo.MineRepo
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module

/*
* koin的mine module
* */
val moduleMine = module {

    //service retrofit
    //single声明单例对象
    // single {
    //     KtRetrofit.initConfig("http://yapi.54yct.com/mock/24/") //baseurl
    //         .getService(MineService::class.java)
    // }
    single {
        get<KtRetrofit> { parametersOf(getBaseHost()) }.getService(MineService::class.java)
    }

    //repo IMineResource
    single { MineRepo(get()) } bind IMineResource::class

    viewModel { MineViewModel(get()) }
}