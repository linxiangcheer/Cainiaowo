package login.repo

import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.LogUtils
import com.cniao5.common.model.SingleLiveData
import com.cniao5.common.network.support.serverData
import com.test.service.net.onBizError
import com.test.service.net.onBizOK
import com.test.service.net.onFailure
import com.test.service.net.onSuccess
import login.net.LoginService
import login.net.LoginReqBody
import login.net.LoginRsp
import login.net.RegisterRsp

/*
* 数据管理类的实现
* */

class LoginRepo(private val service: LoginService) : ILoginResource{

    //内部私有 可以赋值
    private val _registerRsp = SingleLiveData<RegisterRsp>()
    private val _loginRsp = SingleLiveData<LoginRsp>()

    //对外公开,只能观察 不可赋值get put value的
    override val registerRsp: LiveData<RegisterRsp> = _registerRsp
    override val loginRsp: LiveData<LoginRsp> = _loginRsp

    //校验是否注册
    override suspend fun checkRegister(mobi: String) {
        service.isRegister(mobi)
            .serverData()
            .onSuccess {
                onBizError { code, message ->
                    LogUtils.w("是否注册 BizError $code,$message")
                }
                onBizOK<RegisterRsp> { code, data, message ->
                    _registerRsp.value = data
                    LogUtils.i("是否注册 BizOK $data")
                    return@onBizOK
                }
            }
            .onFailure {
                LogUtils.e("是否注册 接口异常 ${it.message}")
            }
    }

    //登录
    override suspend fun requestLogin(body: LoginReqBody) {
        service.login(body)
            .serverData()
            .onSuccess { //接口请求成功
                onBizError { code, message ->
                    LogUtils.w("是否注册 BizError $code,$message")
                }
                onBizOK<LoginRsp> { code, data, message ->
                    _loginRsp.value = data
                    LogUtils.i("是否注册 BizOK $data")
                    return@onBizOK
                }
            }
            .onFailure {
                LogUtils.e("是否注册 接口异常 ${it.message}")
            }
    }

}