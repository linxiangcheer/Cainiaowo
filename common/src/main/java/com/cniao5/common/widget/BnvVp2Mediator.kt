package com.cniao5.common.widget

import android.view.MenuItem
import androidx.core.view.forEachIndexed
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView


/*
*BottomNavigation点击事件和Viewpager2滑动事件绑定，实现同步
* config:() -> Unit 函数体作为一个参数的形式传递给构造函数
* */
class BnvVp2Mediator(private val bnv: BottomNavigationView,
                     private val vp2: ViewPager2,
                    private val config:((BottomNavigationView, ViewPager2) -> Unit) ? = null ) {

    //容器map
    private val map = mutableMapOf<MenuItem, Int>()

    //初始化
    init {
        //拿到循环遍历，构建一个map映射<每个item，每个item的位置>
        bnv.menu.forEachIndexed { index, item ->
            map[item] = index
        }
    }

    //附加/关联
    fun attach(){
        //执行Unit函数体内的动作
        config?.invoke(bnv, vp2)
        vp2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //切换界面的时候，将bnv对应的itemid赋给seleceteditemid然后改变bnv位置
                bnv.selectedItemId = bnv.menu.getItem(position).itemId
            }
        })

        //BottomNavigationView点击事件响应到ViewPager2上
        bnv.setOnNavigationItemSelectedListener { item ->
            vp2.currentItem = map[item] ?: error("Bnv的item的ID${item.itemId}没有对应的ViewPager2元素")
            true
        }
    }

}