package com.cniao5.home.net
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/*
* 首页上方banner图数据
* */
class BannerList : ArrayList<BannerList.BannerListItem>(){
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