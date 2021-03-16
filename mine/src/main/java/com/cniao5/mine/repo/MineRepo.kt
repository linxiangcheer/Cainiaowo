package com.cniao5.mine.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.cniao5.common.network.support.serverData
import com.cniao5.mine.net.MineService
import com.cniao5.mine.net.UserInfoRsp
import com.test.service.net.*


/*
* 获取用户个人信息 实际的数据获取类
* */
class MineRepo(private val service: MineService) :IMineResource{

    private val _userInfoRsp = MutableLiveData<UserInfoRsp>()

    override val liveUserInfo: LiveData<UserInfoRsp> = _userInfoRsp

    override suspend fun getUserInfo(token: String?) {
        service.getUserInfo(token)
            .serverData()
            .onSuccess {
                //只要不是接口响应成功
                onBizError { code, message ->
                    LogUtils.w("获取用户信息 BizError $code,$message")
                }
                onBizOK<UserInfoRsp> { code, data, message ->
                    _userInfoRsp.value = data
                    // LogUtils.i("获取用户信息 BizOK $data")
                    return@onBizOK
                }
            }
            .onFailure {
                LogUtils.e("获取用户信息 接口异常 ${it.message}")
            }
    }
}