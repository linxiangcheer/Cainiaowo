package com.cniao5.study.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.blankj.utilcode.util.LogUtils
import com.cniao5.common.network.support.serverData
import com.cniao5.study.net.BoughtRsp
import com.cniao5.study.net.StudiedRsp
import com.cniao5.study.net.StudyInfoRsp
import com.cniao5.study.net.StudyService
import com.test.service.net.*
import kotlinx.coroutines.flow.Flow

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
                    LogUtils.i("获取学习信息 BizOK $data") //信息
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
                    _studyList.value = data
                    LogUtils.i("获取学习过的课程列表 BizOK $data")
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
                    LogUtils.i("获取购买的课程 BizOK $data")
                    return@onBizOK
                }
            }
            .onFailure {
                // _boughtList.value = null
                LogUtils.e("获取购买的课程 接口异常 ${it.message}")
            }
    }


}

