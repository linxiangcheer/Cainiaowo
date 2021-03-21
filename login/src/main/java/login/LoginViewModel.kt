package login

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseViewModel
import login.net.LoginReqBody
import login.repo.ILoginResource

class LoginViewModel(private val resource: ILoginResource): BaseViewModel() {

    /*
    * ObservableField只有在数据发生改变时UI才会收到通知，而LiveData不同，只要你postValue或者setValue，UI都会收到通知，不管数据有无变化
    * livedata还能感知activity的生命周期，在Activity不活动的时候不会触发
    * */
    val obMobile = ObservableField<String>() //账号
    val obPassword = ObservableField<String>() //密码

    //返回的数据
    val liveRegisterRsp = resource.registerRsp
    val liveLoginRsp = resource.loginRsp

    /*
    * 检查账号是否注册
    * */
    private fun checkRegister(mobi: String) = serverAwait {
        resource.checkRegister(mobi)
    }

    //登录请求
    internal fun repoLogin() {
        val account = obMobile.get() ?: return
        val password = obPassword.get() ?: return
        serverAwait {
            resource.requestLogin(LoginReqBody(account, password))
        }
    }

    /*
    * 登录按钮点击事件
    * 调用登录，两步
    * 1、判断手机号是否已经注册
    * 2、已经注册的，去调用登录接口
    * */
    fun goLogin() {
        val account = obMobile.get() ?: return
        checkRegister(account)
    }

    fun wechat(ctx: Context) {
        ToastUtils.showShort("点击了微信登录${ctx.packageName}")
    }

    fun qq(v: View) {
        ToastUtils.showShort("点击了QQ登录")
    }

    fun weibo() {
        ToastUtils.showShort("点击了微博登录")
    }

    fun AA(view: View) {
        ToastUtils.showShort("静态点击方式")
    }

}