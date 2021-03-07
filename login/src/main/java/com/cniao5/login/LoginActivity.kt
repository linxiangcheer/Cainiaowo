package com.cniao5.login

import androidx.activity.viewModels
import com.cniao5.common.base.BaseActivity
import com.cniao5.login.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels { defaultViewModelProviderFactory }

    override fun getLayoutRes() = R.layout.activity_login

    override fun initView() {
        super.initView()
        mBinding.apply {
            vm = viewModel
        }
    }

    override fun initConfig() {
        super.initConfig()
    }

    override fun initData() {
        super.initData()
    }

}