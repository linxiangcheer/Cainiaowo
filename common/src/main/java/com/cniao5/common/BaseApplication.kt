package com.cniao5.common

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/*
* 抽象Application配置基础通用的设置配置
* */
abstract class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR) //log level error方法，保证这句话不出错不然不写
            //context
            androidContext(this@BaseApplication)
            //依赖注入模块
            //module
        }

        initConfig()
        initData()
    }

    /*
    * protected只有类的成员和继承该类的类才能访问
    * 用于必要的配置初始化
    * */
    protected open fun initData() {  }

    /*
    * 用于子类实现必要的数据初始化
    * */
    protected open fun initConfig() {  }
}