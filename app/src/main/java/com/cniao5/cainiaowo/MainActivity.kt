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

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutRes() = R.layout.activity_main

    override fun initConfig() {
        super.initConfig()
    }

    override fun initView() {
        super.initView()
        val navController= findNavController(R.id.fcv_main)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment, R.id.courseFragment, R.id.studyFragment, R.id.mineFragment
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        mBinding.bnvMain.setupWithNavController(navController)
    }

    override fun initData() {
        super.initData()
    }



}