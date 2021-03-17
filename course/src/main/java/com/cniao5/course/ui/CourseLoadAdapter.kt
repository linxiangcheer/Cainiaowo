package com.cniao5.course.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cniao5.course.databinding.ItemFooterCourseBinding

/*
* 加载数据时下方进度条的adapter
* */
class CourseLoadAdapter(private val adapter: CoursePagingAdapter) :
    LoadStateAdapter<CourseLoadAdapter.FooterVH>() {


    override fun onBindViewHolder(holder: FooterVH, loadState: LoadState) {
        holder.bind(loadState, adapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterVH {
        val binding = ItemFooterCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FooterVH(binding)
    }

    class FooterVH(val binding: ItemFooterCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState, adapter: CoursePagingAdapter) {
            //load的三种状态
            when(loadState){
                is LoadState.Error -> { //加载失败
                    binding.pbFooterCourse.visibility = View.GONE
                    binding.tvFooterCourse.visibility = View.VISIBLE
                    binding.tvFooterCourse.text = "加载失败"
                    binding.tvFooterCourse.setOnClickListener {
                        adapter.retry()
                    }
                }
                is LoadState.Loading -> { //加载中
                    binding.pbFooterCourse.visibility = View.VISIBLE
                    binding.tvFooterCourse.visibility = View.VISIBLE
                    binding.tvFooterCourse.text = "Loading~~"
                }
                is LoadState.NotLoading -> { //停止加载
                    binding.pbFooterCourse.visibility = View.GONE
                    binding.tvFooterCourse.visibility = View.GONE
                }
            }
        }
    }
}