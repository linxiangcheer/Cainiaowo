package com.cniao5.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.home.databinding.ItemJobClassBinding
import com.cniao5.home.net.JobClassList

/*
* 就业班适配器
* */
class JobClassAdapter(private val jobClassList: JobClassList) : RecyclerView.Adapter<JobClassAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH.create(parent)

    override fun onBindViewHolder(holder: JobClassAdapter.VH, position: Int) {
        holder.bind(jobClassList[position])
    }

    override fun getItemCount() = jobClassList.size


    class VH(private val binding: ItemJobClassBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): VH {
                val itemBinding =
                    ItemJobClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return VH(itemBinding)
            }
        }

        fun bind(info: JobClassList.JobClassListItem) {
            binding.url = info.course?.imgUrl //把图片和data binding的url绑定
            itemView.setOnClickListener { //itemView点击事件
                // WebViewActivity.openUrl(it.context, info.course?.h5site ?: "https://m.cniao5.com")
                ToastUtils.showShort("点击事件")
            }
            binding.executePendingBindings()
        }
    }
}