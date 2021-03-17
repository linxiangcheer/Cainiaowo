package com.cniao5.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cniao5.common.base.BaseViewModel
import com.cniao5.course.net.CourseListRsp
import com.cniao5.course.net.CourseTypes
import com.cniao5.course.repo.ICourseResource
import com.cniao5.course.ui.CoursePagingAdapter
import com.cniao5.course.ui.CourseRecycAdapter

class CourseViewModel(val repo: ICourseResource) : BaseViewModel() {

    //Recyclerview的Adapter
    val courseRecycAdapter = CourseRecycAdapter()
    val coursePagingAdapter = CoursePagingAdapter()

    //课程分类
    val liveCourseType: LiveData<CourseTypes?> = repo.liveCourseType

    //获取课程分类
    fun getCourseCategory() = serverAwait {
        repo.getCourseCategory()
    }

    //课程列表
    val liveCourseListRsp: LiveData<CourseListRsp?> = repo.liveCourseListRsp

    //获取课程列表
    fun getCourseList(
        code: String,   //方向从课程分类接口获取    默认 all;例如 android,python
        difficulty: Int,    //难度 (-1 全部) (1 初级) (2 中级) (3 高级) (4 架构) 默认 -1
        is_free: Int,   //价格 (-1, 全部) （0 付费） (1 免费) 默认 -1
        q: Int, //排序  (-1 最新) (1 评价最高)  (2 学习最多) 默认 -1
    ) = serverAwait {
        repo.getCourseList(code, difficulty, is_free, q)
    }

    suspend fun getCourseListPaging(
        code: String,   //方向从课程分类接口获取    默认 all;例如 android,python
        difficulty: Int,    //难度 (-1 全部) (1 初级) (2 中级) (3 高级) (4 架构) 默认 -1
        is_free: Int,   //价格 (-1, 全部) （0 付费） (1 免费) 默认 -1
        q: Int, //排序  (-1 最新) (1 评价最高)  (2 学习最多) 默认 -1
    ) = repo.getCourseListPaging(code, difficulty, is_free, q).cachedIn(viewModelScope) //跟viewModel的生命周期绑定

}