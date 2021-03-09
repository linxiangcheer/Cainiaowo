package login.net

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
/*
* 登录模块相关的结果响应类
* */
@Keep
data class RegisterRsp(
    val is_register: Int = FLAG_UN_REGISTERED //1表示注册，0表示未注册
) {
    companion object {
        const val FLAG_IS_REGISTERED = 1 //已经注册
        const val FLAG_UN_REGISTERED = 0 //未注册
    }
}


/*
* 手机号和密码登录 接口响应
* */
@Keep
data class LoginRsp(val id: Int, //用户id
                    @SerializedName("is_bind_phone")
                    val isBindPhone: Boolean?,
                    val logo_url: String?, //用户头像
                    val token: String?,
                    val username: String? //用户名
                    )