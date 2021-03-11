package com.cniao5.mine.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cniao5.common.base.BaseFragment
import com.cniao5.mine.R
import com.cniao5.mine.databinding.FragmentUserInfoBinding


/*
* 用户信息界面Fragment
* */
class UserInfoFragment : BaseFragment() {

    //获取传过来的Args数据,所有的参数都在args里
    private val args by navArgs<UserInfoFragmentArgs>()

    override fun getLayoutRes() = R.layout.fragment_user_info

    override fun bindView(view: View, savedInstanceState: Bundle?) =
        FragmentUserInfoBinding.bind(view).apply {
            //toolbar返回上个界面
            //toolbar.setupWithNavController(findNavController())
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            //保存按钮
            saveBtn.setOnClickListener { findNavController().navigateUp() }

            info = args.info
        }

}