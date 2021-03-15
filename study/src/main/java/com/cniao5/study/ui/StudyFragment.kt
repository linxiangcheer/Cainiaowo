package com.cniao5.study.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.cniao5.common.base.BaseFragment
import com.cniao5.study.R
import com.cniao5.study.databinding.FragmentStudyBinding
import com.cniao5.study.repo.StudyInfoDbHelper
import com.test.service.repo.DbHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


/*
* 学习中心的Fragmennt
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class StudyFragment: BaseFragment() {

    private val viewModel: StudyViewModel by viewModel()

    // private lateinit var adapter : StudiedAdapter

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_study

    //传入view,将view和databinding绑定到一起
    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        return FragmentStudyBinding.bind(view).apply {
            vm = viewModel //记得加
        }
    }

    override fun initData() {
        super.initData()

        DbHelper.getLiveUserInfo(requireContext()).observeKt {
            // it.let {
            //     viewModel.obUserInfo.set(it) //数据库发生变化的时候拿到Userinfo的值
            //     viewModel.getStudyData()
            // }
            if (it == null) {
                //清空界面上的数据
                StudyInfoDbHelper.deleteStudyInfo(requireContext())
                viewModel.obUserInfo.set(it) //数据库发生变化的时候拿到Userinfo的值
            } else {
                viewModel.obUserInfo.set(it) //数据库发生变化的时候拿到Userinfo的值
                viewModel.getStudyData()
            }
        }

        viewModel.apply {
            //用户学习详情
            liveStudyInfo.observeKt {
                //将数据保存到数据库
                it?.let { StudyInfoDbHelper.insertStudyInfo(requireContext(), it) }
            }
            //已经学习过的课程列表
            liveStudyList.observeKt {
                adapter.submit(it?.datas?: emptyList())
            }
        }

        //studyinfo数据库的数据有变化时触发
        StudyInfoDbHelper.getLiveStudyInfo(requireContext()).observeKt { info ->
            info.let {
                viewModel.liveStudyInfoR.value = info //清空liveStudyInfoR的数据
            }
        }
    }

}
