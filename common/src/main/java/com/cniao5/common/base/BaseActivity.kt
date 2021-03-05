package com.cniao5.common.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/*
* BaseActivity的抽象基类
* */
abstract class BaseActivity: AppCompatActivity(){



    /*
    * 扩展liveData的observer函数
    * */
    protected fun <T: Any> LiveData<T>.observerKt(block:(T) -> Unit) {
        this.observe(this@BaseActivity, Observer { data ->
            // block.invoke(data)
            block(data)
        })
    }

}