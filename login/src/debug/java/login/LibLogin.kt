package login

import login.net.LoginService
import login.repo.ILoginResource
import login.repo.LoginRepo
import com.cniao5.common.network.KtRetrofit
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

/*
* koin注解管理类
* */
val moduleLogin: Module = module {

    //service retrofit
    //single声明单例对象
    single {
        KtRetrofit.initConfig("http://yapi.54yct.com/mock/24/") //baseurl
            .getService(LoginService::class.java)
    }

    //repo ILoginResource
    single { LoginRepo(get()) } bind ILoginResource::class

    //viewModel
    viewModel { LoginViewModel(get()) }
}