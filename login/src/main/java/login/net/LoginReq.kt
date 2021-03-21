package login.net

import androidx.annotation.Keep

/*
* 登录模块相关的请求数据类
* */
@Keep
data class LoginReqBody (
    val mobi: String, //手机号
    val password: String //密码
    )