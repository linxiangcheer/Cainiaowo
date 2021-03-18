package com.cniao5.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.home.databinding.ItemTeacherBinding
import com.cniao5.home.net.TeacherList

/*
* 新上好课/实战推荐适配器
* */
class HomeTeacherAdapter(private val mList: TeacherList) : RecyclerView.Adapter<HomeTeacherAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH.create(parent)

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(mList.datas[position])
    }

    override fun getItemCount() : Int {
        return mList.datas.size
    }


    class VH(private val binding: ItemTeacherBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): VH {
                val itemBinding =
                    ItemTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return VH(itemBinding)
            }
        }

        fun bind(info: TeacherList.Datas) {
            binding.info = info
            itemView.setOnClickListener { //itemView点击事件
                // WebViewActivity.openUrl(it.context, info.course?.h5site ?: "https://m.cniao5.com")
                ToastUtils.showShort("点击事件")
            }
            binding.executePendingBindings()
        }
    }
}