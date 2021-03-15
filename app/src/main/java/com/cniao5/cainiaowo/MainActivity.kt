package com.cniao5.cainiaowo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cniao5.cainiaowo.databinding.ActivityMainBinding
import com.cniao5.common.base.BaseActivity
import com.cniao5.common.widget.BnvVp2Mediator
import com.cniao5.course.CourseFragment
import com.cniao5.home.HomeFragment
import com.cniao5.mine.MineContainerFragment
import com.cniao5.study.ui.StudyFragment

/*
* App主工程的入口界面
* */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutRes() = R.layout.activity_main

    //可以起到复用的效果，首次get对象会初始化，再次get会获取对象的值
    //将索引值和fragment做一个关系映射
    //将Fragment改为ReFragment，  此时Fragment UI不可见就销毁，但保存数据
    private val fragments = mapOf<Int, ReFragment>(
        INDEX_HOME to { HomeFragment() },
        INDEX_COURSE to { CourseFragment() },
        INDEX_STUDY to { StudyFragment() },
        INDEX_MINE to { MineContainerFragment() }
    )

    override fun initConfig() {
        super.initConfig()

    }

    override fun initView() {
        super.initView()

        //初始化控件
        mBinding.apply {
            //viewpager2数据适配器 初始化
            vp2Main.adapter = MainViewPagerAdapter(this@MainActivity, fragments)
            //将bnv和vp2事件绑定
            BnvVp2Mediator(bnvMain, vp2Main) { bnv, vp2 ->
                //viewpager不需要滑动
                vp2.isUserInputEnabled = false
            }.attach()
        }

    }

    //定义一些抽象的常量
    companion object {
        const val INDEX_HOME = 0 //首页home对应的索引位置
        const val INDEX_COURSE = 1 //课程course对应的索引位置
        const val INDEX_STUDY = 2 //学习中心study对应的索引位置
        const val INDEX_MINE = 3 //我的mine对应的索引位置
    }

}

//构造函数没有val代表传参，并不属于类内部的成员变量，加了val内部可以成为类的内部成员属性
class MainViewPagerAdapter(fragmentActivity: FragmentActivity, private val fragments: Map<Int, ReFragment>):
    FragmentStateAdapter(fragmentActivity) {

    //有多少个元素
    override fun getItemCount() = fragments.size

    //创建fragment invoke回调函数，让它实例化创建新的对象
    override fun createFragment(position: Int) = fragments[position]?.invoke() ?: error("请确保fragment数据源和viewPager2的index匹配设置")
}


//类型别名定义 传入的是一个代码块，每次都是一个新的Fragment
typealias ReFragment = () -> Fragment

