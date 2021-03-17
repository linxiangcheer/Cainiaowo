package com.cniao5.home.repo

import androidx.lifecycle.LiveData
import com.cniao5.home.net.BannerList
/*
* 抽象接口
* */
interface IHomeResource {

    //首页上方banner图
    val liveBanner: LiveData<BannerList>

    suspend fun getBanner()

}