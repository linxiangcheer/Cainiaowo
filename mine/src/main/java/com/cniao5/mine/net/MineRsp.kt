package com.cniao5.mine.net
import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.cniao5.mine.repo.UserInfoRspData

import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName


/*
* mine模块，用户个人信息api data解析后的数据类型
* navigation关联布局的传递参数Arguments类型有限制，如果不变成@Parcelize可序列化的数据类型就传不了
* */
// @Parcelize //可序列化的数据类型,必须保证里面的变量都是可序列化的
// @Keep
// data class UserInfoRsp(
//     val company: String?, //公司
//     val desc: String?, //个人介绍
//     val email: String?,
//     @SerializedName("focus_it")
//     val focusIt: String?,
//     @SerializedName("follower_count")
//     val followerCount: Int,
//     @SerializedName("following_count")
//     val followingCount: Int,
//     val id: Int,
//     val job: String?, //职业
//     @SerializedName("logo_url")
//     val logoUrl: String?, //头像url
//     val mobi: String?, //手机号
//     @SerializedName("real_name")
//     val realName: String?, //真实姓名
//     val username: String?, //用户名
//     @SerializedName("work_years")
//     val workYears: String? //工作时间
// ) : Parcelable

typealias UserInfoRsp = UserInfoRspData