package com.cniao5.mine.repo

import android.content.Context
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.room.*
import com.cniao5.mine.net.UserInfoRsp
import com.google.gson.annotations.SerializedName
import com.test.service.repo.UserInfo
import kotlinx.parcelize.Parcelize

/*
* 个人信息的数据类
* PS:数据库的增删改查读写应该放在非UI线程
* */

/*
* mine模块，用户个人信息api data解析后的数据类型
* navigation关联布局的传递参数Arguments类型有限制，如果不变成@Parcelize可序列化的数据类型就传不了
* */

//1、entity的定义
@Entity(tableName = "tv_userinforsp")
@Parcelize //可序列化的数据类型,必须保证里面的变量都是可序列化的
data class UserInfoRspData(
    @PrimaryKey
    val idd: Int, //主键
    val company: String?, //公司
    val desc: String?, //个人介绍
    val email: String?,
    @SerializedName("focus_it")
    val focusIt: String?,
    @SerializedName("follower_count")
    val followerCount: Int,
    @SerializedName("following_count")
    val followingCount: Int,
    val id: Int,
    val job: String?, //职业
    @SerializedName("logo_url")
    val logoUrl: String?, //头像url
    val mobi: String?, //手机号
    @SerializedName("real_name")
    val realName: String?, //真实姓名
    val username: String?, //用户名
    @SerializedName("work_years")
    val workYears: String? //工作时间
): Parcelable

//2、定义Dao层
@Dao
interface UserInfoRspDao {

    //增
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfoRsp(userInfoRsp: UserInfoRsp)

    //改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUserInfoRsp(userInfoRsp: UserInfoRsp)

    //删
    @Delete
    fun deleteUserInfoRsp(userInfoRsp: UserInfoRsp)

    //查，查询数据表有可能为空
    @Query("select * from tv_userinforsp where idd = :idd")
    fun queryLiveUserInfoRsp(idd: Int = 0): LiveData<UserInfoRsp>

    @Query("select * from tv_userinforsp where idd = :idd")
    fun queryUserInfoRsp(idd: Int = 0): UserInfoRsp

}

//3、database定义数据库
@Database(entities = [UserInfoRsp::class], version = 1, exportSchema = false)
abstract class UserInfoRspDB : RoomDatabase() {

    abstract fun userinforspDao(): UserInfoRspDao

    companion object {
        private const val DB_NAME = "userinforsp_db"

        @Volatile //保证不同线程对这个共享变量进行操作的可见性，并且禁止进行指令重排序.
        private var instance: UserInfoRspDB ?= null

        //保证线程安全
        @Synchronized
        fun getInstance(context: Context): UserInfoRspDB {
            return instance ?: Room.databaseBuilder(
                context,
                UserInfoRspDB::class.java,
                DB_NAME
            ).build().also { instance = it }
        }

    }

}




























