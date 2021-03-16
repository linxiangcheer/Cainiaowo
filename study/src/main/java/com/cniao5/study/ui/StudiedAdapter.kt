package com.cniao5.study.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cniao5.study.databinding.ItemCourseStudyBinding
import com.cniao5.study.net.StudiedRsp

class StudiedAdapter : RecyclerView.Adapter<StudiedVH>() {

    private val mList = mutableListOf<StudiedRsp.Data>()

    /*recyclerview初始化*/
    fun submit(list: List<StudiedRsp.Data>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudiedVH.createVH(parent)

    override fun onBindViewHolder(holder: StudiedVH, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size

}

/*
* 比较通用的recyclerview.viewholder创建方式
* */
class StudiedVH(private val binding: ItemCourseStudyBinding) : RecyclerView.ViewHolder(binding.root){

    companion object {
        fun createVH(parent: ViewGroup): StudiedVH {
            return StudiedVH(
                ItemCourseStudyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun bind(info: StudiedRsp.Data) {
        binding.info = info
        //把获取到的progress进度条数据给进度条 也可以用扩展函数app:progress_current实现
        // binding.npbProgressItemStudy.progress = info.progress.toInt()
        binding.executePendingBindings()
    }

}
