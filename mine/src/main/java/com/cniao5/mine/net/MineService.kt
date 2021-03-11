package com.cniao5.mine.net

import com.test.service.net.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


/*
* Mine模块相关的网络接口
* */

interface MineService {

    /*
    * 用户详情信息的获取
    * */
    @GET("member/userinfo")
    fun getUserInfo(@Header("token") token: String?) : Call<BaseResponse>

}