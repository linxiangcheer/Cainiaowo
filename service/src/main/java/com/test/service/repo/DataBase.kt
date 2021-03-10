package com.test.service.repo

import android.content.Context
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.annotations.SerializedName

/*
* 菜鸟App的公共业务基础数据类
* 数据库的增删改查读写应该放在非UI线程
* */

//3、database定义数据库
//exportSchema是否导出数据表
@Database(entities = [UserInfo::class], version = 1, exportSchema = false)
abstract class DataBase : RoomDatabase() {

    abstract val userDao: UserDao

    //单例
    companion object {
        private const val CNIAO_DB_NAME = "cniao_db"

        @Volatile //保证不同线程对这个共享变量进行操作的可见性，并且禁止进行指令重排序
        private var instance: DataBase? = null

        @Synchronized //保证线程安全
        fun getInstance(context: Context): DataBase {
            return instance ?: Room.databaseBuilder(
                context,
                DataBase::class.java,
                CNIAO_DB_NAME
            ).build().also { instance = it }
        }
    }
}

//1、entity的定义
@Entity(tableName = "tv_user")
data class UserInfo(
    @PrimaryKey
    val id: Int, //主键
    @Embedded //内嵌的数据表，User的字段将会被添加到表tv_user中
    val loginRsp: LoginRsp?
) {
    @Keep
    data class LoginRsp (
        @ColumnInfo(name = "user_id")
        val id: Int, //用户id
        @SerializedName("is_bind_phone")
        val isBindPhone: Boolean?,
        val logo_url: String?, //用户头像
        val token: String?,
        val username: String? //用户名
            )
}

//2、dao层的定义
@Dao
interface UserDao {

    //增
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserInfo)

    //改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: UserInfo)

    //删
    @Delete
    fun deleteUser(user: UserInfo)

    //条件判断： =精确判断  in多种条件的判断  like模糊的判断
    //查数据表有可能为空
    @Query("select * from tv_user where id = :id")
    fun queryLiveUser(id: Int = 0): LiveData<UserInfo>

    @Query("select * from tv_user where id = :id")
    fun queryUser(id: Int = 0): UserInfo

}