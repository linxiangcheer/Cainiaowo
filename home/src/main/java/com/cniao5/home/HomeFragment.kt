package com.cniao5.home

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.cniao5.common.base.BaseFragment
import com.cniao5.home.databinding.FragmentHomeBinding

/*
* 主页的Fragment
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class HomeFragment: BaseFragment() {

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_home

    //传入view,将view和databinding绑定到一起
    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        return FragmentHomeBinding.bind(view)
    }

}