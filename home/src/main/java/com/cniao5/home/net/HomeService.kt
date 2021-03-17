package com.cniao5.home.net

import com.test.service.net.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/*
* 主页模块的接口
* */
interface HomeService {

    /*
    * 首页上方banner
    * */
    @GET("ad/new/banner/list")
    fun getBanner(
        @Query("type")type: Int = 2,   //类型 1:小程序 2:web 3:h5 4:ios 5:android 如: 2表示web 默认2
        @Query("page_show") pageshow:Int = 1  //页面显示 1 首页 2 课程 3 大数据学院 4 机器人学院 5 人工智能学院 6 推广员 默认1
    ) : Call<BaseResponse>

}