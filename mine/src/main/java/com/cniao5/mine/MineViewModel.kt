package com.cniao5.mine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cniao5.common.base.BaseViewModel
import com.test.service.repo.UserInfo

/*
* Mine模块的viewModel
* */
class MineViewModel: BaseViewModel() {

    /*
    * MutableLiveData：整个实体类或者数据类型变化后才通知.不会细节到某个字段。
    * */
    val liveUser = MutableLiveData<UserInfo>()

}