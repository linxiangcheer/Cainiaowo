package com.cniao5.mine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseViewModel
import com.cniao5.mine.net.UserInfoRsp
import com.cniao5.mine.repo.IMineResource
import com.test.service.repo.UserInfo

/*
* Mine模块的viewModel
* */
class MineViewModel(private val repo: IMineResource): BaseViewModel() {

    //UserInfo的数据
    // val liveUser = MutableLiveData<UserInfo>()

    //用在userInfoRspFragment中
    val liveInforep: LiveData<UserInfoRsp> = repo.liveUserInfo

    //用于布局的Livedata
    val liveInfoRsp = MutableLiveData<UserInfoRsp>()

    /*
    * 获取用户信息
    * */
    fun getUserInfo(token: String?) {
        serverAwait {
            repo.getUserInfo(token)
        }
    }


}