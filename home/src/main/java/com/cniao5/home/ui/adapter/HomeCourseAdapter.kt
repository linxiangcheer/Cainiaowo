package com.cniao5.home.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.home.databinding.ItemHomeCourseBinding
import com.cniao5.home.net.HomeCourseItem
import com.cniao5.home.net.NewClassList

/*
* 新上好课/实战推荐适配器
* */
class HomeCourseAdapter(private val mList: List<HomeCourseItem>) : RecyclerView.Adapter<HomeCourseAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH.create(parent)

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size


    class VH(private val binding: ItemHomeCourseBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): VH {
                val itemBinding =
                    ItemHomeCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return VH(itemBinding)
            }
        }

        fun bind(info: HomeCourseItem) {
            binding.info = info
            binding.tvOldPriceItemCourse.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG //中划线
            itemView.setOnClickListener { //itemView点击事件
                // WebViewActivity.openUrl(it.context, info.course?.h5site ?: "https://m.cniao5.com")
                ToastUtils.showShort("点击事件")
            }
            binding.executePendingBindings()
        }
    }
}