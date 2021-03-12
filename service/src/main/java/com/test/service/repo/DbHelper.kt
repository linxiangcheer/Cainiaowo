package com.test.service.repo

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*
* 数据库操作帮助类
* */

object DbHelper {

    /*
    * 获取room数据表中存储的userInfo
    * return liveData观察者形式
    * */
    fun getLiveUserInfo(context: Context): LiveData<UserInfo> = DataBase.getInstance(context).userDao().queryLiveUser()

    /*
    * 以普通数据对象的形式，获取userInfo
    * */
    fun getUserInfo(context: Context) = DataBase.getInstance(context).userDao().queryUser()

    /*
    * 删除数据表中的userInfo信息
    * */
    fun deleteUserInfo(context: Context) {
        //要指定Dispatchers.IO线程,用户网络请求和文件访问
        GlobalScope.launch(Dispatchers.IO) {
            getUserInfo(context)?.let { info ->
                DataBase.getInstance(context).userDao().deleteUser(info)
            }

        }
    }

    /*
    * 新增用户数据到数据表
    * */
    fun insertUserInfo(context: Context, user: UserInfo) {
        //要指定Dispatchers.IO线程,用户网络请求和文件访问
        GlobalScope.launch(Dispatchers.IO) {
            DataBase.getInstance(context).userDao().insertUser(user)
        }
    }




}