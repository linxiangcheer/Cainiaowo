package com.cniao5.course.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cniao5.course.databinding.ItemCourseRecycBinding
import com.cniao5.course.net.CourseListRsp

class CourseRecycAdapter : RecyclerView.Adapter<CourseVH>() {

    private val mList = mutableListOf<CourseListRsp.Data>()

    /*
    * recyclervice
    * clear 是否清空list初始化
    * */
    fun submitClear(list: List<CourseListRsp.Data>, clear: Boolean = false) {
        if (clear) mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CourseVH.createVH(parent)

    override fun onBindViewHolder(holder: CourseVH, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size
}


/*
* 比较通用的recyclerview.viewholder创建方式
* */
class CourseVH(private val binding: ItemCourseRecycBinding) : RecyclerView.ViewHolder(binding.root){

    companion object {
        fun createVH(parent: ViewGroup): CourseVH {
            return CourseVH(
                ItemCourseRecycBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun bind(info: CourseListRsp.Data) {
        binding.info = info
        binding.tvOldPriceItemCourse.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //实现删除线的效果
        binding.executePendingBindings() //更新将表达式绑定到已修改变量的任何视图
    }

}