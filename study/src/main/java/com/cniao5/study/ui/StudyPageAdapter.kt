package com.cniao5.study.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cniao5.study.databinding.ItemCourseStudyBinding
import com.cniao5.study.net.StudiedRsp

/*
* 列表适配器 RecyclerView.ViewHolder用作其视图支架类型
* paging3 分页库 用于加载本地/网络数据显示
* */
class StudyPageAdapter : PagingDataAdapter<StudiedRsp.Data, StudiedVH>(differCallback) {

    override fun onBindViewHolder(holder: StudiedVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudiedVH.createVH(parent)

    //用于区分是不是相同的数据
    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<StudiedRsp.Data>() {
            override fun areItemsTheSame(
                oldItem: StudiedRsp.Data,
                newItem: StudiedRsp.Data
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StudiedRsp.Data,
                newItem: StudiedRsp.Data
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}