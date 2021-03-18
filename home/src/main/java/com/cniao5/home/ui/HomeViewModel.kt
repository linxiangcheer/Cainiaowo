package com.cniao5.home.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cniao5.common.base.BaseViewModel
import com.cniao5.common.model.DataResult
import com.cniao5.home.net.BannerList
import com.cniao5.home.repo.IHomeResource
import com.test.service.net.BaseResponse

/*
* 首页的viewmodel
* */
class HomeViewModel(private val resource: IHomeResource) : BaseViewModel() {

    val liveBanner = resource.liveBanner    //首页上方的banner图

    val liveHomeList = resource.liveHomeList    //首页模块名字、请求地址列表

    //首页上方banner图的网络请求
    fun getBanner() = serverAwait {
        resource.getBanner()
    }

    //首页模块名字、请求地址列表
    fun getHomeList() = serverAwait {
        resource.getHomeList()
    }

    //首页各个模块的详情信息，返回完整的数据，在fragment中解析分类
    // suspend fun getModuleDatas(moduleid: Int) = resource.getModuleDatas(moduleid)

    //在viewmodel中判断需要进行哪个请求 type 1 = 讲师   2 = 课程推荐   5 = 就业班
    //根据模块id来判断需要进行哪个请求
    suspend fun getModuleType(type: Int, page: Int = 1, size: Int = 10, courseid: Int = 10119, moduleid: Int = 1) : DataResult<BaseResponse>? {
        return when(type) {
            TYPE_TEACHER -> resource.getTeacherList(page, size) //讲师
            TYPE_COURSE -> resource.getHomeCourse(courseid) //课程推荐
            TYPE_JOB -> resource.getJobDatas(moduleid) //就业班
            else -> null
        }
    }

    companion object {
        internal const val TYPE_TEACHER = 1
        internal const val TYPE_COURSE = 2
        internal const val TYPE_JOB = 5
    }


}