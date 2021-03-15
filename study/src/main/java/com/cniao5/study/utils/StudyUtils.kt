package com.cniao5.study.utils

import androidx.databinding.BindingAdapter
import com.cniao5.study.net.StudiedRsp
import com.daimajia.numberprogressbar.NumberProgressBar

/**
 *
 */
object StudyUtils {
    @JvmStatic
    fun rankStr(rank: Int): String {
        return if (rank > 0) "第${rank}名" else "千里之外"
    }


    @JvmStatic
    fun parseStudentComment(info: StudiedRsp.Data?): String {
        return "${info?.lessonsPlayedTime} ${info?.commentCount}人评价"
    }

    @JvmStatic
    fun parseFree(info: StudiedRsp.Data?): String {
        return if (info?.isFree == 1) "免费" else "￥${info?.nowPrice}"
    }

    /*
    * 有两种格式的返回值，判断该用哪种
    * */
    @JvmStatic
    fun parseTitle(info: StudiedRsp.Data?): String {
        return if (info?.course != null) {
            "${info.course.name}"
        } else {
            "${info?.name}"
        }
    }

    /*
    * 有两种格式的返回值，判断该用哪种
    * */
    @JvmStatic
    fun parseImage(info: StudiedRsp.Data?): String {
        return if (info?.course != null) {
            "${info.course.imgUrl}"
        } else {
            "${info?.imgUrl}"
        }
    }

}

@BindingAdapter("app:progress_current", requireAll = false)
fun setProgress(pb: NumberProgressBar, progress: Double?) {
    pb.progress = ((progress ?: 0.0) * 100).toInt()
}