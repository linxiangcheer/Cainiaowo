package login

import com.cniao5.common.BaseApplication
import com.test.service.moduleService
import org.koin.core.context.loadKoinModules

class LoginApplication : BaseApplication() {


    override fun initConfig() {
        super.initConfig()
        loadKoinModules(moduleService)
        loadKoinModules(moduleLogin)
    }


}