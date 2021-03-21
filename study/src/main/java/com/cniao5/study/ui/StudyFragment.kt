package com.cniao5.study.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseFragment
import com.cniao5.study.R
import com.cniao5.study.StudyViewModel
import com.cniao5.study.databinding.FragmentStudyBinding
import com.cniao5.study.repo.StudyInfoDbHelper
import com.google.android.material.tabs.TabLayout
import com.test.service.repo.DbHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


/*
* 学习中心的Fragmennt
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class StudyFragment : BaseFragment() {

    private val viewModel: StudyViewModel by viewModel()

    // private lateinit var adapter : StudiedAdapter

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_study

    //传入view,将view和databinding绑定到一起
    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        return FragmentStudyBinding.bind(view).apply {
            vm = viewModel //记得加 关联viewmodel对象

            //tablayout点击事件
            tlStudy.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                //tab被选的时候回调
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    //页面position从0开始
                    // Log.d("yyy","${tab!!.position}")
                }

                //tab未被选择时回调
                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                //tab重新选择时回调
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }


    }

    override fun initConfig() {
        super.initConfig()

        DbHelper.getLiveUserInfo(requireContext()).observeKt {
            // it.let {
            //     viewModel.obUserInfo.set(it) //数据库发生变化的时候拿到Userinfo的值
            //     viewModel.getStudyData()
            // }
            if (it == null) {
                //清空界面上的数据
                StudyInfoDbHelper.deleteStudyInfo(requireContext())
                viewModel.obUserInfo.set(it) //数据库发生变化的时候拿到Userinfo的值
                viewModel.adapter.submit(emptyList()) //清空recyclerview的数据
            } else {
                viewModel.obUserInfo.set(it) //数据库发生变化的时候拿到Userinfo的值
                viewModel.getStudyData()
                // viewModel.adapterPaging.refresh() //paging3手动的下拉刷新
            }
        }

        viewModel.apply {
            //用户学习详情
            liveStudyInfo.observeKt {
                //将数据保存到数据库
                it?.let { StudyInfoDbHelper.insertStudyInfo(requireContext(), it) }
            }
            //已经学习过的课程列表 使用传统加载数据的方式实现
            liveStudyList.observeKt {
                adapter.submit(it?.datas ?: emptyList())
            }

            //paging分页库加载数据方式实现 挂起函数需要在协程里启动
            //当控制这个LifecycleCoroutineScope的生命周期至少处于Lifecycle. state时，启动并运行给定的块
            // lifecycleScope.launchWhenCreated {
            //     //观察pagingdata
            //     pagingData().observeKt {
            //         it?.let {
            //             adapterPaging.submitData(lifecycle, it)
            //         }
            //     }
            // }

        }

        //studyinfo数据库的数据有变化时触发 用户信息
        StudyInfoDbHelper.getLiveStudyInfo(requireContext()).observeKt { info ->
            viewModel.liveStudyInfoR.value = info //清空liveStudyInfoR的数据
        }
    }


    override fun initData() {
        super.initData()
    }

}
