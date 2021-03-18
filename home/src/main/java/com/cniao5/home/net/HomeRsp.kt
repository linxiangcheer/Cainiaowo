package com.cniao5.home.net

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/*
* 首页上方banner图数据
* */
class BannerList : ArrayList<BannerList.BannerListItem>() {
    @Keep
    data class BannerListItem(
        @SerializedName("client_url")
        val clientUrl: Any?,
        @SerializedName("created_time")
        val createdTime: String?,
        val id: Int,
        @SerializedName("img_url")
        val imgUrl: String?,
        val name: Any?,
        @SerializedName("order_num")
        val orderNum: Int,
        @SerializedName("page_show")
        val pageShow: Int,
        @SerializedName("redirect_url")
        val redirectUrl: String?,
        val state: Int,
        val type: String?
    )
}

/*
* 首页模块名字、请求地址列表
* */
class HomeList : ArrayList<HomeList.HomeListItem>() {
    @Keep
    data class HomeListItem(
        @SerializedName("created_time")
        val createdTime: String?,
        @SerializedName("data_url")
        val dataUrl: String?,   // 无用，调用模块组件列表接口
        val id: Int,    // 模块ID
        @SerializedName("is_show_more")
        val isShowMore: Int,    // 是否显示更多按钮
        val layout: Int,    // 布局方式，0：单行布局 1：多行布局（可由客户端自己决定布局方式）
        @SerializedName("more_redirect_url")
        val moreRedirectUrl: Any?,  // 跳转更多的地址
        val scroll: Int,    // 是否滚动 0不滚动  1单行滚动 2双行滚动
        @SerializedName("sub_title")
        val subTitle: Any?,  // 子标题
        val title: String?,  // 模块标题
        val type: Int   // 模块类型： (1 老师) (2 课程) (3 banner) (4 ad) (5 班级) (6 合作伙伴) (7 内容块) (8 图标) (9 学员故事)
    )
}

//就业班 type = 1
class JobClassList : ArrayList<JobClassList.JobClassListItem>() {
    @Keep
    data class JobClassListItem(
        @SerializedName("apply_deadline_time")
        val applyDeadlineTime: String?,
        @SerializedName("balance_payment_time")
        val balancePaymentTime: Any?,
        @SerializedName("button_desc")
        val buttonDesc: Any?,
        val course: Course?,
        @SerializedName("created_time")
        val createdTime: String?,
        @SerializedName("current_price")
        val currentPrice: Int,
        @SerializedName("graduate_time")
        val graduateTime: String?,
        val id: Int,
        @SerializedName("is_apply_stop")
        val isApplyStop: Int,
        @SerializedName("learning_mode")
        val learningMode: Int,
        @SerializedName("lessons_count")
        val lessonsCount: Any?,
        val number: Int,
        @SerializedName("original_price")
        val originalPrice: Int,
        @SerializedName("start_class_time")
        val startClassTime: String?,
        val status: Int,
        @SerializedName("stop_use_down_payment_time")
        val stopUseDownPaymentTime: Any?,
        @SerializedName("student_count")
        val studentCount: Int,
        @SerializedName("student_limit")
        val studentLimit: Int,
        @SerializedName("study_expiry_day")
        val studyExpiryDay: Int,
        @SerializedName("teacher_ids")
        val teacherIds: String?,
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
    }
}

//课程推荐 type = 2
class NewClassList : ArrayList<HomeCourseItem>() //新上好课
class LimitFreeList : ArrayList<HomeCourseItem>() //限时免费
class CombatList : ArrayList<HomeCourseItem>() //实战推荐

@Keep
data class HomeCourseItem(
    val brief: String?,
    @SerializedName("comment_count")
    val commentCount: Int,
    @SerializedName("cost_price")
    val costPrice: Int,
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
    @SerializedName("lessons_count")
    val lessonsCount: Int,
    @SerializedName("lessons_played_time")
    val lessonsPlayedTime: Int,
    val name: String?,
    @SerializedName("now_price")
    val nowPrice: Int
) {
    @Keep
    data class FirstCategory(
        val code: String?,
        val id: Int,
        val title: String?
    )
}

//人气讲师 type = 5
// @Keep
// data class TeacherList(
//     val datas: List<Data>?,
//     val page: Int,
//     val size: Int,
//     val total: Int,
//     @SerializedName("total_page")
//     val totalPage: Int
// ) {
//     @Keep
//     data class Data(
//         val brief: String?,
//         val company: String?,
//         val id: Int,
//         @SerializedName("job_title")
//         val jobTitle: String?,
//         @SerializedName("logo_url")
//         val logoUrl: String?,
//         @SerializedName("teacher_name")
//         val teacherName: String?
//     )
// }

@Keep
data class TeacherList(
    val datas: List<Datas>,
    val page: Int,
    val size: Int,
    val total: Int,
    @SerializedName("total_page")
    val totalPage: Int
) {
    @Keep
    data class Datas(
        val brief: String?,
        val company: String?,
        val id: Int,
        @SerializedName("job_title")
        val jobTitle: String?,
        @SerializedName("logo_url")
        val logoUrl: String?,
        @SerializedName("teacher_name")
        val teacherName: String?
    )
}
