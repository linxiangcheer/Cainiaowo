package com.cniao5.course.ui.playvideo

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cniao5.course.R
import com.cniao5.course.net.CourseDetails

/*
* 播放目录列表的适配器  创建前判断是否为空，空的话不创建
* */
class ChapterAdapter(private val mList: ArrayList<CourseDetails.CourseDetailsItem?>): BaseExpandableListAdapter() {

    lateinit var groupViewHolder: GroupViewHolder
    lateinit var childViewHolder: ChildViewHoler

    //返回组item数量
    override fun getGroupCount() = mList.size

    //返回对应下标的组item下面的所有子item数量
    override fun getChildrenCount(groupPosition: Int): Int {
        return mList[groupPosition]?.lessons?.size ?: 0
    }

    //根据下标返回对应的组item
    override fun getGroup(groupPosition: Int): Any {
        return mList[groupPosition]!!
    }

    //返回指定下标的子item
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return mList[groupPosition]?.lessons?.get(childPosition)!!
    }

    //返回指定下标的组item的id,保证id唯一
    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    //返回指定下标的子item的id,保证唯一
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return (groupPosition+childPosition).toLong()
    }

    //指示子id和组id在对基础数据的更改之间是否稳定
    override fun hasStableIds(): Boolean {
        return true
    }

    //加载组item的视图，isExpanded表示是否处于展开状态
    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var mconvertView = convertView
        if (convertView == null) {
            mconvertView = LayoutInflater.from(parent?.context).inflate(R.layout.item_parent_chapter, parent, false)
            groupViewHolder = GroupViewHolder(mconvertView)
            mconvertView?.tag = groupViewHolder
        } else {
            groupViewHolder = mconvertView?.tag as GroupViewHolder
        }

        //组item的标题
        groupViewHolder.tv_name_des?.text = mList[groupPosition]?.title

        //是否处于展开状态
        if (isExpanded) {
            groupViewHolder.iv_super?.setImageResource(R.drawable.ic_back)
        } else {
            groupViewHolder.iv_super?.setImageResource(R.drawable.ic_down)
        }

        return mconvertView!!
    }

    //加载子item的视图
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var mconvertView = convertView
        if (convertView == null) {
            mconvertView = LayoutInflater.from(parent?.context).inflate(R.layout.item_child_chapter, parent, false)
            childViewHolder = ChildViewHoler(mconvertView)
            //用mconvertView代替convertView不然会有空指针异常
            mconvertView?.tag = childViewHolder
        } else {
            childViewHolder = mconvertView?.tag as ChildViewHoler
        }

        //子item显示的文字
        childViewHolder.tv_name_sub?.text = mList[groupPosition]?.lessons?.get(childPosition)?.name

        return mconvertView!!
    }

    //指定位置的子节点是否可选
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }


    inner class GroupViewHolder(view: View) {
        var tv_name_des: TextView? = null
        var iv_super: ImageView? = null
        init {
            tv_name_des = view.findViewById(R.id.id_parent_tv)
            iv_super = view.findViewById(R.id.id_parent_iv)
        }
    }

    inner class ChildViewHoler(view: View) {
        var tv_name_sub: TextView? = null
        init {
            tv_name_sub = view.findViewById(R.id.id_child_tv)
        }
    }


}