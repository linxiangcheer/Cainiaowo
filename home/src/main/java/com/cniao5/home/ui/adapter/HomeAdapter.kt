package com.cniao5.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cniao5.home.databinding.ItemHomeBinding
import com.cniao5.home.net.*
import com.cniao5.home.ui.HomeFragment
import kotlinx.coroutines.Job

/*
* 首页主RecyclerView适配器，里边还有adapter
* */
class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeAdapterVH>() {

    private val homeList = ArrayList<HomeFragment.HomeData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HomeAdapterVH.create(parent)

    override fun onBindViewHolder(holder: HomeAdapterVH, position: Int) {
        holder.bind(homeList[position])
    }

    override fun getItemCount() = homeList.size

    //更新Recyclerview数据
    fun upRecyclerViewList(homeList: ArrayList<HomeFragment.HomeData>) {
        this.homeList.clear()
        this.homeList.addAll(homeList)
        notifyDataSetChanged()
    }

    //清空RecyclerView数据
    fun clearRecyclerViewList() {
        this.homeList.clear()
        notifyDataSetChanged()
    }

    //Recyclerview中嵌套Recyclerview
    class HomeAdapterVH(val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): HomeAdapterVH {
                val mBinding =
                    ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HomeAdapterVH(mBinding)
            }
        }

        fun bind(homeData: HomeFragment.HomeData) {
            binding.title = homeData.title
            setAdapter(homeData)
            binding.executePendingBindings()
        }

        //判断应该创建哪种布局
        private fun setAdapter(item: HomeFragment.HomeData) {

            binding.rvItemHome.adapter = when(item.id) {
                HomeFragment.ID_JOB_CLASS -> { //即将开班
                    binding.rvItemHome.layoutManager = GridLayoutManager(itemView.context, 2) //多少列
                    val doubleDataList = JobClassList()
                    doubleDataList.addAll(item.obj as JobClassList) //todo 传过来只有三张图，复制多一份数据
                    doubleDataList.addAll(item.obj as JobClassList)
                    JobClassAdapter(doubleDataList) //把数据给jobClassAdapter
                }
                HomeFragment.ID_NEW_CLASS -> { //新开好课
                    //VERTICAL垂直的
                    binding.rvItemHome.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
                    HomeCourseAdapter(item.obj as NewClassList)
                }
                HomeFragment.ID_COMBIT -> { //实战推荐
                    //VERTICAL垂直的
                    binding.rvItemHome.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
                    HomeCourseAdapter(item.obj as CombatList)
                }
                HomeFragment.ID_LIMIT_FREE -> { //限时免费
                    //VERTICAL垂直的
                    binding.rvItemHome.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
                    HomeCourseFreeAdapter(item.obj as LimitFreeList)
                }
                HomeFragment.ID_POP_TEACHER -> { //金牌讲师
                    binding.rvItemHome.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
                    HomeTeacherAdapter(item.obj as TeacherList)
                }
                else -> error("error type ${item.id}")
            }

        }

    }


}