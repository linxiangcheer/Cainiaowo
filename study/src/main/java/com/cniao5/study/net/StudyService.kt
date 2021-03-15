package com.cniao5.study.net

import com.test.service.net.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
* 学习中心模块的接口
* */

interface StudyService {

    //用户学习详情
    @GET("member/study/info")
    fun getStudyInfo(): Call<BaseResponse>

    //用户学习过的课程列表
    @GET("member/courses/studied")
    fun getStudyList(
        @Query("page")page: Int = 1,
        @Query("size")size: Int = 10
    ): Call<BaseResponse>

    /*
    * 用户购买的课程
    * page默认为1
    * size默认为10
    * */
    @GET("member/courses/bought")
    fun getBoughtCourse(
        @Query("page")page: Int = 1,
        @Query("size")size: Int = 10
    ): Call<BaseResponse>

}