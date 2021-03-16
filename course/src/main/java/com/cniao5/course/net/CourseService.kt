package com.cniao5.course.net

import com.test.service.net.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
* 课程模块的接口
* */
interface CourseService {

    /*
    * 获取课程分类
    * */
    @GET("course/category")
    fun getCourseCategory(): Call<BaseResponse>

    /*
    * 获取课程列表
    * */
    @GET("course/v2/list")
    fun getCourseList(
        @Query("course_type") course_type: Int = -1, // 类型 (-1 全部) (1 普通课程) (2 职业课程/班级课程) (3 实战课程) 默认 -1
        @Query("code") code: String = "all", // 方向 (-1 全部) 从课程分类接口获取    默认 all
        @Query("difficulty") difficulty: Int = -1, // 难度 (-1 全部) (1 初级) (2 中级) (3 高级) (4 架构) 默认 -1
        @Query("is_free") is_free: Int = -1, // 价格 (-1, 全部) （0 付费） (1 免费) 默认 -1
        @Query("q") q: Int = -1, // 排序方式  (-1 最新) (1 评价最高)  (2 学习最多) 默认 -1
        @Query("page") page: Int = 1, // 页码 默认1
        @Query("size") size: Int = 20 // 每页显示数 默认20
    ): Call<BaseResponse>
    /*
    * "code": "actual_combat","title": "实战"
      "code": "bd","title": "大数据"
      "code": "android","title": "Android"
      "code": "python",title": "Python"
      "code": "java"，"title": "Java"
    * */


}