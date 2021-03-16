package com.cniao5.course.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.blankj.utilcode.util.LogUtils
import com.cniao5.common.network.support.serverData
import com.cniao5.course.net.CourseListRsp
import com.cniao5.course.net.CourseService
import com.cniao5.course.net.CourseTypes
import com.test.service.net.onBizError
import com.test.service.net.onBizOK
import com.test.service.net.onFailure
import com.test.service.net.onSuccess
import kotlinx.coroutines.flow.Flow

class CourseResource(val service: CourseService) : ICourseResource {

    //课程分类
    private val _liveCourseType = MutableLiveData<CourseTypes?>()
    override val liveCourseType: LiveData<CourseTypes?>
        get() = _liveCourseType

    override suspend fun getCourseCategory() {
        service.getCourseCategory()
            .serverData()
            .onSuccess {
                onBizOK<CourseTypes> { code, data, message ->
                    _liveCourseType.value = data
                    LogUtils.i("获取课程分类 BizOK $data")
                }
                onBizError { code, message ->
                    LogUtils.w("获取课程分类 BizError $code,$message")
                }
            }
            .onFailure {
                LogUtils.e("获取课程分类 接口异常 ${it.message}")
            }
    }


    private val pageSize = 10
    override suspend fun getCourseList(
        course_type: Int,
        code: String,
        difficulty: Int,
        is_free: Int,
        q: Int
    ): Flow<PagingData<CourseListRsp.Data>> {
        TODO("Not yet implemented")
    }
}