package com.cniao5.mine

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.cniao5.common.base.BaseFragment
import com.cniao5.mine.databinding.FragmentMineBinding

/*
* 我的 Fragment
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class MineFragment: BaseFragment() {

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_mine

    //传入view,将view和databinding绑定到一起
    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        return FragmentMineBinding.bind(view)
    }

}