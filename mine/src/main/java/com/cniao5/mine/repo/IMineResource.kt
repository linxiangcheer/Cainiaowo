package com.cniao5.mine.repo

import androidx.lifecycle.LiveData
import com.cniao5.mine.net.UserInfoRsp


/*
* Mine模块的数据获取 接口
* */
interface IMineResource {

    //用户信息的返回数据类 livedata
    val liveUserInfo: LiveData<UserInfoRsp>

    //获取userinfo的api函数
    suspend fun getUserInfo(token: String?)

}