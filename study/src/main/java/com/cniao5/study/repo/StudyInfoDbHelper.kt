package com.cniao5.study.repo

import android.content.Context
import com.cniao5.study.net.StudyInfoDB
import com.cniao5.study.net.StudyInfoRsp
import com.test.service.repo.DbHelper.getUserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object StudyInfoDbHelper {
    /**
     * 以普通数据对象的形式，获取数据
     */
    fun getStudyInfo(context: Context) = StudyInfoDB.getInstance(context).studyInfoDao().queryStudyInfo()

    /**
     * 获取room数据表中存储的数据
     * return liveData形式
     */
    fun getLiveStudyInfo(context: Context) = StudyInfoDB.getInstance(context).studyInfoDao().queryLiveData()

    /**
     * 删除数据表中的信息
     */
    fun deleteStudyInfo(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            getStudyInfo(context)?.apply {
                StudyInfoDB.getInstance(context).studyInfoDao().deleteStudyInfo(this)
            }
        }
    }

    /**
     * 新增用户数据到数据表
     */
    fun insertStudyInfo(context: Context, info: StudyInfoRsp) {
        GlobalScope.launch(Dispatchers.IO) {
            StudyInfoDB.getInstance(context).studyInfoDao().insertStudyInfo(info)
        }
    }

}