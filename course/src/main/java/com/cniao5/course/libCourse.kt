package com.cniao5.course

import com.cniao5.common.network.KtRetrofit
import com.cniao5.common.utils.getBaseHost
import com.cniao5.course.net.CourseService
import com.cniao5.course.repo.CourseResource
import com.cniao5.course.repo.ICourseResource
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module

/*
* 课程模块的 module
* */
val moduleCourse = module {

    //service retrofit
    // single声明单例对象
    // single {
    //     KtRetrofit.initConfig("https://course.api.cniao5.com/") //baseurl
    //         .getService(CourseService::class.java)
    // }

    single {
        get<KtRetrofit> { parametersOf(getBaseHost()) }.getService(CourseService::class.java)
    }

    //repo IMineResource
    single { CourseResource(get()) } bind ICourseResource::class

    viewModel { CourseViewModel(get()) }

}