package com.cniao5.study.ui

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.cniao5.common.base.BaseViewModel
import com.cniao5.study.net.BoughtRsp
import com.cniao5.study.net.StudiedRsp
import com.cniao5.study.net.StudyInfoRsp
import com.cniao5.study.repo.StudyResource
import com.test.service.repo.UserInfo

class StudyViewModel(private val resource: StudyResource) : BaseViewModel() {

    //用户学习详情
    val liveStudyInfo: LiveData<StudyInfoRsp> = resource.liveStudyInfo //这个数据用来观察
    val liveStudyInfoR = MutableLiveData<StudyInfoRsp>() //这个是界面布局数据
    //已经学习过的课程列表
    val liveStudyList: LiveData<StudiedRsp> = resource.liveStudyList
    //已经购买的课程，班级 信息
    val liveBoughtList: LiveData<BoughtRsp> = resource.liveBoughtList

    //用户信息
    val obUserInfo = ObservableField<UserInfo>()

    //我的学习列表适配器
    val adapter = StudiedAdapter()

    //请求数据
    fun getStudyData() = serverAwait {
        resource.getStudyInfo()
        resource.getStudyList()
        // resource.getBoughtCourse()
    }


}