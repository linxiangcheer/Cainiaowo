package com.cniao5.course.net

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/*
* 课程 数据返回类型
* */

/*
* 课程分类
* */
class CourseTypes: ArrayList<CourseTypes.CourseTypeItem>(){
    @SuppressLint("ParcelCreator") //忽略注释元素的警告
    @Parcelize //可序列化的数据类型,必须保证里面的变量都是可序列化的
    @Keep
    data class CourseTypeItem(
        val code: String?, //课程类型，用于分类列表获取
        val id: Int, //类型id
        val title: String? //类型Title
    ) : Parcelable
}



/*
* 课程列表，用于课程中心
* */
@Keep
data class CourseListRsp(
    val datas: List<Data>?,
    val page: Int,
    val size: Int,
    val total: Int,
    @SerializedName("total_page")
    val totalPage: Int
) {
    @Keep
    data class Data(
        val brief: String?, //  简介
        @SerializedName("comment_count")
        val commentCount: Int, //  精选评论数量
        @SerializedName("cost_price")
        val costPrice: Int, //  原价
        @SerializedName("expiry_day")
        val expiryDay: Int, //  课程学习有效期（天）
        @SerializedName("finished_lessons_count")
        val finishedLessonsCount: Int, // 已更新的课时数
        @SerializedName("first_category")
        val firstCategory: FirstCategory?, //  一级分类
        val id: Int, //  课程ID
        @SerializedName("img_url")
        val imgUrl: String?, //  封面
        @SerializedName("is_free")
        val isFree: Int, // 是否是免费课程 1是 0否
        @SerializedName("is_live")
        val isLive: Int, //  是否是直播课程
        @SerializedName("is_pt")
        val isPt: Boolean,  // 是否参加了拼团活动
        @SerializedName("is_share_card")
        val isShareCard: Boolean,  // 是否加入了学习邀请卡活动
        @SerializedName("lessons_count")
        val lessonsCount: Int, // 总课时
        @SerializedName("lessons_played_time")
        val lessonsPlayedTime: Int, // 学习人数
        val name: String?, // 名字
        @SerializedName("now_price")
        val nowPrice: Double // 当前价格
    ) {
        @Keep
        data class FirstCategory( //  一级分类
            val code: String?,
            val id: Int,
            val title: String?
        )
    }
}