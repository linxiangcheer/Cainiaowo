package com.cniao5.mine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cniao5.common.base.BaseViewModel
import com.test.service.repo.UserInfo

/*
* Mine模块的viewModel
* */
class MineViewModel: BaseViewModel() {

    val liveUser = MutableLiveData<UserInfo>()

}