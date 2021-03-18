package com.cniao5.home.utils

import com.cniao5.home.net.HomeCourseItem


/*
 */
object HomeUtils {

    @JvmStatic
    fun parseStudentComment(info: HomeCourseItem?): String {
        return "${info?.lessonsCount} ${info?.commentCount}人评价"
    }

    /*
    * 是否免费 免费返回true 否则返回false
    * */
    @JvmStatic
    fun parseFree(info: HomeCourseItem?): Boolean {
        return info?.isFree == 1
    }

}