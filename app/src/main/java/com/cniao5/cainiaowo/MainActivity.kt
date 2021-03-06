package com.cniao5.cainiaowo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cniao5.cainiaowo.databinding.ActivityMainBinding
import com.cniao5.common.base.BaseActivity
import com.cniao5.common.ktx.application
import com.test.service.assistant.AssistantApp

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutRes() = R.layout.activity_main

    override fun initConfig() {
        super.initConfig()
    }

    override fun initView() {
        super.initView()
        val navController= findNavController(R.id.fcv_main)
        mBinding.bnvMain.setupWithNavController(navController)
    }

    override fun initData() {
        super.initData()
    }



}