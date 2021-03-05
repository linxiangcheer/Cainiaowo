package com.cniao5.common

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/*
*
* 抽象的公用BaseApplication*/
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR) //log level error方法，保证这句话不出错不然不写

            androidContext(this@BaseApplication)
        }
    }
}