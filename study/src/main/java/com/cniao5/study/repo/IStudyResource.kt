package com.cniao5.study.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.cniao5.study.net.BoughtRsp
import com.cniao5.study.net.StudiedRsp
import com.cniao5.study.net.StudyInfoRsp
import kotlinx.coroutines.flow.Flow

/*
* 学习中心模块相关的抽象数据接口
* */
interface IStudyResource {

    val liveStudyInfo: LiveData<StudyInfoRsp>
    val liveStudyList: LiveData<StudiedRsp>
    val liveBoughtList: LiveData<BoughtRsp>

    //用户学习详情
    suspend fun getStudyInfo()

    //用户学习过的课程列表
    suspend fun getStudyList()

    //用户购买的课程
    suspend fun getBoughtCourse()

}