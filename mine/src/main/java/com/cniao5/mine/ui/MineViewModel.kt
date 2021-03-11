package com.cniao5.mine.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cniao5.common.base.BaseViewModel
import com.cniao5.mine.repo.IMineResource
import com.test.service.repo.UserInfo

/*
* Mine模块的viewModel
* */
class MineViewModel(private val repo: IMineResource): BaseViewModel() {

    //用在userInfoFragment中
    val liveInfo = repo.liveUserInfo

    /*
    * 获取用户信息
    * */
    fun getUserInfo(token: String?) {
        serverAwait {
            repo.getUserInfo(token)
        }
    }




}