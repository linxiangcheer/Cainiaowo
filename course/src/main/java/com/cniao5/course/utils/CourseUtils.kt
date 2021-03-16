package com.cniao5.course.utils

import com.cniao5.course.net.CourseListRsp


/*
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年10月30日 05:21
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 */
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
    * 是否免费
    * */
    @JvmStatic
    fun parseFree(info: CourseListRsp.Data?): String {
        return if (info?.isFree == 1) "免费" else "￥${info?.nowPrice}"
    }
}