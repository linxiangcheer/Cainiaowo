package login.net

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.test.service.repo.UserInfo

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
//Room类型别名
typealias LoginRsp = UserInfo.LoginRsp