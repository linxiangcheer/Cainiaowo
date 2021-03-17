package com.cniao5.course.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cniao5.course.databinding.ItemCourseRecycBinding
import com.cniao5.course.net.CourseListRsp

class CoursePagingAdapter : PagingDataAdapter<CourseListRsp.Data, CourseViewHolder>(differCallback){

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        //在第几项触发的加载
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder.create(parent)
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<CourseListRsp.Data>() {
            override fun areItemsTheSame(
                oldItem: CourseListRsp.Data,
                newItem: CourseListRsp.Data
            ) = oldItem.id == newItem.id
            //            ) = false

            override fun areContentsTheSame(
                oldItem: CourseListRsp.Data,
                newItem: CourseListRsp.Data
            ) = oldItem == newItem
        }

    }

}

class CourseViewHolder(private val binding: ItemCourseRecycBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): CourseViewHolder {
            return CourseViewHolder(
                ItemCourseRecycBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun bind(data: CourseListRsp.Data?) {
        binding.info = data
        binding.tvOldPriceItemCourse.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //实现删除线的效果
        binding.executePendingBindings()
    }
}