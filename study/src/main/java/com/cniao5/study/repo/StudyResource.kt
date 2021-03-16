package com.cniao5.study.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.cniao5.common.network.support.serverData
import com.cniao5.study.net.BoughtRsp
import com.cniao5.study.net.StudiedRsp
import com.cniao5.study.net.StudyInfoRsp
import com.cniao5.study.net.StudyService
import com.google.gson.Gson
import com.test.service.net.*
import kotlinx.coroutines.flow.Flow
import java.lang.Math.random
import kotlin.Exception

class StudyResource(private val service: StudyService) : IStudyResource {

    private val _studyInfo = MutableLiveData<StudyInfoRsp>()
    private val _studyList = MutableLiveData<StudiedRsp>()
    private val _boughtList = MutableLiveData<BoughtRsp>()

    override val liveStudyInfo: LiveData<StudyInfoRsp> = _studyInfo
    override val liveStudyList: LiveData<StudiedRsp> = _studyList
    override val liveBoughtList: LiveData<BoughtRsp> = _boughtList

    /*
    * 获取学习信息
    * */
    override suspend fun getStudyInfo() {
        service.getStudyInfo()
            .serverData()
            .onSuccess {
                //只要不是接口响应成功
                onBizError { code, message ->
                    // _studyInfo.value = null
                    LogUtils.w("获取学习信息 BizError $code,$message") //警告
                }
                onBizOK<StudyInfoRsp> { code, data, message ->
                    _studyInfo.value = data
                    // LogUtils.i("获取学习信息 BizOK $data") //信息
                    return@onBizOK
                }
            }
            .onFailure {
                // _studyInfo.value = null
                LogUtils.e("获取学习信息 接口异常 ${it.message}") //错误
            }
    }

    /*
    * 获取学习过的课程列表
    * */
    override suspend fun getStudyList() {
        service.getStudyList()
            .serverData()
            .onSuccess {
                onBizError { code, message ->
                    LogUtils.w("获取学习过的课程列表 BizError $code,$message")
                }
                onBizOK<StudiedRsp> { code, data, message ->
                    // _studyList.value = data

                    /*
                    * 这里将data.datas的类型改为MutableList可变list，用addAll添加元素
                    * 执行2次 共8条数据
                    * */
                    for(i in 0..2) {
                        data?.datas?.addAll(data.datas!!)
                        random()
                    }
                    _studyList.value = data
                    // LogUtils.i("获取学习过的课程列表 BizOK $data")
                    return@onBizOK
                }
            }
            .onFailure {
                LogUtils.e("获取学习过的课程列表 接口异常 ${it.message}")
            }
    }

    /*
    * 获取购买的课程
    * */
    override suspend fun getBoughtCourse() {
        service.getBoughtCourse()
            .serverData()
            .onSuccess {
                onBizError { code, message ->
                    // _boughtList.value = null
                    LogUtils.w("获取购买的课程 BizError $code,$message")
                }
                onBizOK<BoughtRsp> { code, data, message ->
                    _boughtList.value = data
                    // LogUtils.i("获取购买的课程 BizOK $data")
                    return@onBizOK
                }
            }
            .onFailure {
                // _boughtList.value = null
                LogUtils.e("获取购买的课程 接口异常 ${it.message}")
            }
    }


    /*
    * 将studyPageSource转化为flow数据
    * */
    private val pageSize = 100
    override suspend fun pagingData(): Flow<PagingData<StudiedRsp.Data>> {
        val config = PagingConfig(
            pageSize = pageSize, // 每页显示的数据的大小。对应 PagingSource 里 LoadParams.loadSize
            prefetchDistance = 5, // 预刷新的距离，距离最后一个 item 多远时加载数据，默认为 pageSize
            initialLoadSize = 10,  // 初始化加载数量，默认为 pageSize * 3
            maxSize = pageSize * 3 // 一次应在内存中保存的最大数据，默认为 Int.MAX_VALUE
        )
        // 数据源，要求返回的是 PagingSource 类型对象
        return Pager(config = config, null) {
            StudyItemPagingSource(service)
        }.flow // 最后构造的和外部交互对象，有 flow 和 liveData 两种
    }

}

/*
* 处理分页逻辑
* */
class StudyItemPagingSource(val service: StudyService) : PagingSource<Int, StudiedRsp.Data>() {
    /*
    * 该办法只在初始加载成功且加载页面的列表不为空的情况下被调用
    *
    * 如果您的应用程序需要支持从网络增量加载到本地数据库，则必须为从用户的滚动位置锚点开始的恢复分页提供支持
    * 先从本地数据库加载数据，然后在数据库用完数据后从网络加载数据
    * */
    override fun getRefreshKey(state: PagingState<Int, StudiedRsp.Data>): Int? = null

    //用这个方法来触发异步加载
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StudiedRsp.Data> {
        var result: LoadResult<Int, StudiedRsp.Data> =
            LoadResult.Error(Exception("加载中..."))
        var firstPage = params.key ?: 1
        service.getStudyList(firstPage, params.loadSize) //初始化加载数量
            .serverData()
            .onSuccess {
                onBizError { code, message ->
                    LogUtils.w("获取学习过的课程列表 BizError $code,$message")
                    result = LoadResult.Error(Exception(message))
                }
                onBizOK<StudiedRsp> { code, data, message ->
                    // LogUtils.i("获取学习过的课程列表 BizOK $data")
                    val totalPage = data?.totalPage ?: 0 //total页码
                    //加载下一页的key，如果传null就说明到底了
                    val nextPage = if (firstPage <= totalPage) {
                        // Log.d("yyy","课程列表还没到底")
                        firstPage++
                    } else {
                        // Log.d("yyy","课程列表到底啦")
                        null
                    }
                    result = LoadResult.Page<Int, StudiedRsp.Data>(
                        data?.datas ?: emptyList(), //如果datas为空就回传一个空的只读列表
                    null,
                        nextPage
                    )
                }
            }
            .onFailure {
                LogUtils.e("获取学习过的课程列表 接口异常 ${it.message}")
                result = LoadResult.Error(it)
            }
        return result
    }

}

