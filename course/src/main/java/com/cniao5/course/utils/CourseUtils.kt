package com.cniao5.course.utils

import com.cniao5.course.net.CourseListRsp



/*
* 工具类
* */
object CourseUtils {

    /*
    * 总课时 + 评价人数
    * */
    @JvmStatic
    fun parseStudentComment(info: CourseListRsp.Data?): String {
        return "${info?.lessonsCount}  ${info?.commentCount}人评价"
    }

    /*
    * 是否免费 免费返回true 否则返回false
    * */
    @JvmStatic
    fun parseFree(info: CourseListRsp.Data?): Boolean {
        return info?.isFree == 1
    }
}