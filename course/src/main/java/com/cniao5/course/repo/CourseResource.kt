package com.cniao5.course.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.blankj.utilcode.util.LogUtils
import com.cniao5.common.network.support.serverData
import com.cniao5.course.net.CourseDetails
import com.cniao5.course.net.CourseListRsp
import com.cniao5.course.net.CourseService
import com.cniao5.course.net.CourseTypes
import com.test.service.net.onBizError
import com.test.service.net.onBizOK
import com.test.service.net.onFailure
import com.test.service.net.onSuccess
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class CourseResource(val service: CourseService) : ICourseResource {

    //课程分类
    private val _liveCourseType = MutableLiveData<CourseTypes?>()
    override val liveCourseType: LiveData<CourseTypes?>
        get() = _liveCourseType

    //课程列表
    private val _liveCourseListRsp = MutableLiveData<CourseListRsp?>()
    override val liveCourseListRsp: LiveData<CourseListRsp?>
        get() = _liveCourseListRsp

    //课程播放目录列表
    private val _liveCourseDetails = MutableLiveData<CourseDetails?>()
    override val liveCourseDetails: LiveData<CourseDetails?>
        get() = _liveCourseDetails

    //课程分类
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

    //课程列表
    override suspend fun getCourseList(
        code: String, //方向从课程分类接口获取    默认 all;例如 android,python
        difficulty: Int, // 难度 (-1 全部) (1 初级) (2 中级) (3 高级) (4 架构) 默认 -1
        is_free: Int, // 价格 (-1, 全部) （0 付费） (1 免费) 默认 -1
        q: Int // 排序方式  (-1 最新) (1 评价最高)  (2 学习最多) 默认 -1
    ) {
        service.getCourseList(-1, code, difficulty, is_free, q ,1, 20)
            .serverData()
            .onSuccess {
                onBizError { code, message ->
                    LogUtils.w("获取课程列表 BizError $code,$message")
                }
                onBizOK<CourseListRsp> { code, data, message ->
                    _liveCourseListRsp.value = data
                    LogUtils.i("获取课程列表 BizOK $data")
                }
            }
            .onFailure {
                LogUtils.e("获取课程列表 接口异常 ${it.message}")
            }
    }

    /*
    * 课程列表的paging3
    * */
    private val pageSize = 10
    override suspend fun getCourseListPaging(
        code: String,
        difficulty: Int,
        is_free: Int,
        q: Int
    ): Flow<PagingData<CourseListRsp.Data>> {
        val pagingConfig = PagingConfig(
            pageSize = pageSize, // 每页显示的数据的大小。对应 PagingSource 里 LoadParams.loadSize
            prefetchDistance = 2, // 预刷新的距离，距离最后一个 item 多远时加载数据，默认为 pageSize
            initialLoadSize = 10,  // 初始化加载数量，默认为 pageSize * 3
            maxSize = pageSize * 3 // 一次应在内存中保存的最大数据，默认为 Int.MAX_VALUE
        )
        return Pager(config = pagingConfig, null) {
            CourseListPagingSource(service, code, difficulty, is_free, q)
        }.flow
    }

    /*
    * 课程列表的Source
    * */
    class CourseListPagingSource constructor(
        private val service: CourseService,
        // private val course_type: Int,//类型 (-1 全部) (1 普通课程) (2 职业课程/班级课程) (3 实战课程) 默认 -1
        private val code: String,//方向从课程分类接口获取    默认 all;例如 android,python
        private val difficulty: Int,//难度 (-1 全部) (1 初级) (2 中级) (3 高级) (4 架构) 默认 -1
        private val is_free: Int,//价格 (-1, 全部) （0 付费） (1 免费) 默认 -1
        private val q: Int,//排序  (-1 最新) (1 评价最高)  (2 学习最多) 默认 -1
    ) : PagingSource<Int, CourseListRsp.Data>() {
        override fun getRefreshKey(state: PagingState<Int, CourseListRsp.Data>): Int? = null

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CourseListRsp.Data> {
            var result: LoadResult<Int, CourseListRsp.Data> =
                LoadResult.Error(Exception("加载中"))
            var firstPage = params.key ?: 1
            service.getCourseList(-1, code, difficulty, is_free, q ,1, 20)
                .serverData()
                .onSuccess {
                    onBizError { code, message ->
                        LogUtils.w("获取课程列表Paging3 BizError $code,$message")
                        result = LoadResult.Error(Exception(message))
                    }
                    onBizOK<CourseListRsp> { code, data, message ->
                        LogUtils.i("获取课程列表Paging3 BizOK $data")
                        val totalPage = data?.totalPage ?: 0

                        //判断数据是否还有数据 todo正式开发要
                        // val nextPage = if (firstPage <= totalPage) {
                        //     firstPage++ //数据没到底
                        // } else {
                        //     null //数据到底了
                        // }

                        result = LoadResult.Page (
                            data = data?.datas ?: emptyList(),
                            prevKey = if (firstPage == 1) null else firstPage - 1, //todo 正式开发这里为null
                            nextKey = if (firstPage < totalPage) firstPage + 1 else null //todo 正式开发这里为nextPage
                                )
                    }
                }
                .onFailure {
                    LogUtils.e("获取课程列表Paging3 接口异常 ${it.message}")
                    result = LoadResult.Error(it)
                }
            return result
        }

    }


    /*
    * 课程播放目录列表
    * */
    override suspend fun getCourseDetails(course_id: Int) {
        service.getCourseDetails(course_id)
            .serverData()
            .onSuccess {
                onBizError { code, message ->
                    LogUtils.w("获取课程播放目录 BizError $code,$message")
                }
                onBizOK<CourseDetails> { code, data, message ->
                    _liveCourseDetails.value = data
                    LogUtils.i("获取课程播放目录 BizOK $data")
                }
            }
            .onFailure {
                LogUtils.e("获取课程播放目录 接口异常 ${it.message}")
            }
    }

}

