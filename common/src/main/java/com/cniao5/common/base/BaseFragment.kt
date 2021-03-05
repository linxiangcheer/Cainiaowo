package com.cniao5.common.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/*
* Fragment的抽象基类
* */
abstract class BaseFragment: Fragment {

    constructor(): super()

    constructor(@LayoutRes layout: Int) : super(layout)

    /*
    * 扩展liveData的observer函数
    * */
    protected fun <T: Any> LiveData<T>.observerKt(block:(T) -> Unit) {
        this.observe(viewLifecycleOwner, Observer { data ->
            // block.invoke(data)
            block(data)
        })
    }

}