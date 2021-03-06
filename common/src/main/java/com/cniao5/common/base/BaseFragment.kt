package com.cniao5.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/*
* Fragment的抽象基类
* */
abstract class BaseFragment: Fragment {

    constructor(): super()

    constructor(@LayoutRes layout: Int) : super(layout)

    private var mBinding: ViewDataBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //将Binding对象和view关联到一起
        mBinding = bindView(view, savedInstanceState)
        mBinding?.lifecycleOwner = viewLifecycleOwner
        initConfig()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        //不再观察布局绑定状态
        mBinding?.unbind()
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding

    /*
    * open可以被子类重写 默认fun不能被重写和继承
    * 初始化配置
    * */
    open fun initConfig() { }

    //初始化数据
    open fun initData() { }

    /*
    * 扩展liveData的observer函数
    * livedata回传回来的数据可能为null
    * */
    protected fun <T: Any> LiveData<T>.observerKt(block:(T?) -> Unit) {
        this.observe(viewLifecycleOwner, Observer { data ->
            // block.invoke(data)
            block(data)
        })
    }

}