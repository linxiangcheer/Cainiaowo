package com.cniao5.course

import androidx.lifecycle.ViewModel
import com.cniao5.common.base.BaseViewModel
import com.cniao5.course.net.CourseDetails
import com.cniao5.course.repo.ICourseResource

class PlayVideoViewModel(val service: ICourseResource): BaseViewModel() {

    val liveCourseDetails = service.liveCourseDetails

    fun getCourseDetails(course_id: Int) = serverAwait {
        service.getCourseDetails(course_id)
    }

    var arrayLiveCourseDetails = ArrayList<CourseDetails.CourseDetailsItem?>()


}