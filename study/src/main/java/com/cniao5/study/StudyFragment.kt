package com.cniao5.study

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.cniao5.common.base.BaseFragment
import com.cniao5.study.databinding.FragmentStudyBinding


/*
* 学习中心的Fragmennt
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class StudyFragment: BaseFragment() {

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_study

    //传入view,将view和databinding绑定到一起
    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        return FragmentStudyBinding.bind(view)
    }

}