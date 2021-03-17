package com.cniao5.home.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cniao5.common.base.BaseViewModel
import com.cniao5.home.net.BannerList
import com.cniao5.home.repo.IHomeResource

/*
* 首页的viewmodel
* */
class HomeViewModel(private val resource: IHomeResource) : BaseViewModel() {

    //首页上方的banner图
    val liveBanner = resource.liveBanner
    val liveBannerMu =  MutableLiveData<BannerList>()

    fun getBanner() = serverAwait {
        resource.getBanner()
    }

}