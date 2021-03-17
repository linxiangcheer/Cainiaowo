package com.cniao5.home.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.cniao5.common.network.support.serverData
import com.cniao5.home.net.BannerList
import com.cniao5.home.net.HomeService
import com.test.service.net.onBizError
import com.test.service.net.onBizOK
import com.test.service.net.onFailure
import com.test.service.net.onSuccess

class HomeResource (val service: HomeService) : IHomeResource {

    private val _liveBanner = MutableLiveData<BannerList>()

    override val liveBanner: LiveData<BannerList>
        get() = _liveBanner

    /*
    * 获取首页上方banner图数据
    * */
    override suspend fun getBanner() {
        service.getBanner()
            .serverData()
            .onSuccess {
                onBizError { code, message ->
                    LogUtils.w("获取banner信息 onBizzError $code$message")
                }
                onBizOK<BannerList> { code, data, message ->
                    _liveBanner.value = data
                    LogUtils.i("获取banner信息 onBizzOK $data")
                }
            }
            .onFailure {
                LogUtils.e("获取banner信息 接口异常 ${it.message}")
            }
    }

}