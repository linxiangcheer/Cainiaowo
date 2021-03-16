package com.cniao5.study.net

import android.content.Context
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.room.*

import com.google.gson.annotations.SerializedName


/*
* 学习中心页面相关的数据返回类
* */

/*
* 用户学习详情
* member/study/info
* */
@Keep
@Entity(tableName = "tb_studyinforsp")
data class StudyInfoRsp(
    @PrimaryKey
    val id: Int, //主键
    val rank: Int,
    @SerializedName("today_study_time")
    val todayStudyTime: Int,
    @SerializedName("total_study_time")
    val totalStudyTime: Int
)

/*
* 用户学习详情的Dao层
* */
@Dao
interface StudyInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudyInfo(studyinfo: StudyInfoRsp)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateStudyInfo(studyinfo: StudyInfoRsp)

    @Delete
    fun deleteStudyInfo(studyinfo: StudyInfoRsp)

    //= in like
    @Query("select * from tb_studyinforsp where id =:id")
    fun queryStudyInfo(id: Int = 0): StudyInfoRsp?

    @Query("select * from tb_studyinforsp where id =:id")
    fun queryLiveData(id: Int = 0): LiveData<StudyInfoRsp>
}

/*
* 用户学习详情的数据库
* */
@Database(entities = [StudyInfoRsp::class], version = 1, exportSchema = false)
abstract class StudyInfoDB : RoomDatabase() {
    abstract fun studyInfoDao():StudyInfoDao

    companion object {
        private const val DB_NAME = "study_data"

        @Volatile
        private var instance: StudyInfoDB? = null

        @Synchronized
        fun getInstance(context: Context): StudyInfoDB {
            return instance ?: Room.databaseBuilder(
                context,
                StudyInfoDB::class.java,
                DB_NAME
            ).build().also { instance = it }
        }
    }
}

/*
* 已经学习过的课程列表
* member/courses/studied
* 1、定义entity
* */
@Keep
// @Entity(tableName = "tb_studiedrsp")
data class StudiedRsp(
    var datas: MutableList<Data>?, //todo 修改成可变
    val page: Int,
    val size: Int,
    val total: Int,
    @SerializedName("total_page")
    val totalPage: Int
) {
    @Keep
    data class Data(
        val brief: String?,
        @SerializedName("comment_count")
        val commentCount: Int,
        @SerializedName("cost_price")
        val costPrice: Int,
        val course: Course?,
        @SerializedName("course_type")
        val courseType: Int,
        @SerializedName("current_price")
        val currentPrice: Int,
        @SerializedName("first_category")
        val firstCategory: FirstCategory?,
        @SerializedName("get_method")
        val getMethod: Int,
        val id: Int,
        @SerializedName("img_url")
        val imgUrl: String?,
        @SerializedName("is_distribution")
        val isDistribution: Boolean,
        @SerializedName("is_free")
        val isFree: Int,
        @SerializedName("is_live")
        val isLive: Int,
        @SerializedName("is_pt")
        val isPt: Boolean,
        @SerializedName("left_expiry_days")
        val leftExpiryDays: Int,
        @SerializedName("lessons_count")
        val lessonsCount: Any?,
        @SerializedName("lessons_played_time")
        val lessonsPlayedTime: Int,
        val name: String?,
        @SerializedName("now_price")
        val nowPrice: Int,
        val number: Int,
        @SerializedName("original_price")
        val originalPrice: Int,
        val progress: Double,
        val title: String?
    ) {
        @Keep
        data class Course(
            val h5site: String?,
            val id: Int,
            @SerializedName("img_url")
            val imgUrl: String?,
            val name: String?,
            val website: String?
        )

        @Keep
        data class FirstCategory(
            val code: String?,
            val id: Int,
            val title: String?
        )
    }
}
// data class StudiedRsp(
//     @PrimaryKey
//     val idd: Int, //主键
//     var datas: MutableList<Data>?,
//     val page: Int,
//     val size: Int,
//     val total: Int,
//     @SerializedName("total_page")
//     val totalPage: Int
// ) {
//     @Keep
//     data class Data(
//         val brief: String?,
//         @SerializedName("comment_count")
//         val commentCount: Int,
//         @SerializedName("cost_price")
//         val costPrice: Int,
//         val course: Course?,
//         @SerializedName("course_type")
//         val courseType: Int,
//         @SerializedName("current_price")
//         val currentPrice: Int,
//         @SerializedName("first_category")
//         val firstCategory: FirstCategory?,
//         @SerializedName("get_method")
//         val getMethod: Int,
//         val id: Int,
//         @SerializedName("img_url")
//         val imgUrl: String?,
//         @SerializedName("is_distribution")
//         val isDistribution: Boolean,
//         @SerializedName("is_free")
//         val isFree: Int,
//         @SerializedName("is_live")
//         val isLive: Int,
//         @SerializedName("is_pt")
//         val isPt: Boolean,
//         @SerializedName("left_expiry_days")
//         val leftExpiryDays: Int,
//         @SerializedName("lessons_count")
//         val lessonsCount: Any?,
//         @SerializedName("lessons_played_time")
//         val lessonsPlayedTime: Int,
//         val name: String?,
//         @SerializedName("now_price")
//         val nowPrice: Int,
//         val number: Int,
//         @SerializedName("original_price")
//         val originalPrice: Int,
//         val progress: Double,
//         val title: String?
//     ) {
//         @Keep
//         data class Course(
//             val h5site: String?,
//             val id: Int,
//             @SerializedName("img_url")
//             val imgUrl: String?,
//             val name: String?,
//             val website: String?
//         )
//
//         @Keep
//         data class FirstCategory(
//             val code: String?,
//             val id: Int,
//             val title: String?
//         )
//     }
// }

// //2、定义dao
// @Dao
// interface StudiedDao {
//     @Insert(onConflict = OnConflictStrategy.REPLACE)
//     fun insertStudied(studiedRsp: StudiedRsp)
//
//     @Update(onConflict = OnConflictStrategy.REPLACE)
//     fun updateStudied(studiedRsp: StudiedRsp)
//
//     @Delete
//     fun deleteStudied(studiedRsp: StudiedRsp)
//
//     //= in like
//     @Query("select * from tb_studiedrsp where idd =:idd")
//     fun queryStudied(idd: Int = 0): StudiedRsp?
//
//     @Query("select * from tb_studiedrsp where idd =:idd")
//     fun queryStudiedLiveData(idd: Int = 0): LiveData<StudiedRsp>
// }
//
// //3、定义数据库
// @Database(entities = [StudiedRsp::class], version = 1, exportSchema = false)
// abstract class StudiedRspDB : RoomDatabase() {
//     abstract fun studiedDao() : StudiedDao
//
//     //单例
//     companion object {
//         private const val DB_NAME = "studied_data"
//
//         @Volatile
//         private var instance: StudiedRspDB ?= null
//
//         @Synchronized
//         fun getInstance(context: Context): StudiedRspDB {
//             return instance ?: Room.databaseBuilder(
//                 context,
//                 StudiedRspDB::class.java,
//                 DB_NAME
//             ).build().also { instance = it }
//         }
//     }
// }


/*
* 已经购买的课程，班级 信息
* member/courses/bought
* */
@Keep
data class BoughtRsp(
    val datas: List<Data>?,
    val page: Int,
    val size: Int,
    val total: Int,
    @SerializedName("total_page")
    val totalPage: Int
) {
    @Keep
    data class Data(
        @SerializedName("cancel_time")
        val cancelTime: String?,
        val course: Course?,
        @SerializedName("created_time")
        val createdTime: String?,
        @SerializedName("get_method")
        val getMethod: Int,
        val id: Int,
        @SerializedName("is_expired")
        val isExpired: Boolean,
        @SerializedName("left_expiry_days")
        val leftExpiryDays: Int,
        @SerializedName("order_time")
        val orderTime: String?,
        @SerializedName("product_id")
        val productId: Int,
        @SerializedName("product_type")
        val productType: Int
    ) {
        @Keep
        data class Course(
            val brief: Any?,
            @SerializedName("comment_count")
            val commentCount: Int,
            @SerializedName("cost_price")
            val costPrice: Int,
            val course: Course?,
            @SerializedName("first_category")
            val firstCategory: FirstCategory?,
            val id: Int,
            @SerializedName("img_url")
            val imgUrl: String?,
            @SerializedName("is_distribution")
            val isDistribution: Boolean,
            @SerializedName("is_free")
            val isFree: Int,
            @SerializedName("is_live")
            val isLive: Int,
            @SerializedName("is_pt")
            val isPt: Boolean,
            @SerializedName("lessons_played_time")
            val lessonsPlayedTime: Int,
            val name: String?,
            @SerializedName("now_price")
            val nowPrice: Int,
            val number: Int,
            val progress: Int,
            val title: String?
        ) {
            @Keep
            data class Course(
                val h5site: String?,
                val id: Int,
                @SerializedName("img_url")
                val imgUrl: String?,
                val name: String?,
                val website: String?
            )

            @Keep
            data class FirstCategory(
                val code: String?,
                val id: Int,
                val title: String?
            )
        }
    }
}

