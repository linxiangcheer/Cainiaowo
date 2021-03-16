package com.cniao5.course.ui

import androidx.lifecycle.LiveData
import com.cniao5.common.base.BaseViewModel
import com.cniao5.course.net.CourseTypes
import com.cniao5.course.repo.ICourseResource

class CourseViewModel(val repo: ICourseResource) : BaseViewModel() {

    //课程分类
    val liveCourseType: LiveData<CourseTypes?> = repo.liveCourseType

    //获取课程分类
    fun getCourseCategory() = serverAwait {
        repo.getCourseCategory()
    }



}