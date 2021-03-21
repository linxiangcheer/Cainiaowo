package com.cniao5.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.cniao5.common.ktx.bindView
import com.cniao5.common.ktx.immediateStatusBar

/*
* BaseActivity的抽象基类
* */
abstract class BaseActivity<ActBinding: ViewDataBinding>: AppCompatActivity{

    constructor(): super()

    constructor(@LayoutRes layout: Int) : super(layout)

    //protected子类可以抽取对象 private不行
    protected lateinit var mBinding: ActBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = bindView<ActBinding>(getLayoutRes())
        initView()
        initConfig()
        initData()
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    //初始化视图
    open fun initView() { }

    //open才能被子类重写 默认fun不能被重写和继承
    //初始化配置
    open fun initConfig() { }

    //初始化数据
    open fun initData() { }

    override fun onDestroy() {
        super.onDestroy()
        //用lateinit初始化的字段最好要做判断有没有成功
        if (this::mBinding.isInitialized){
            mBinding.unbind()
        }
    }

    /*
    * 扩展liveData的observer函数
    * */
    protected inline fun <T: Any?> LiveData<T>.observerKt(crossinline block:(T?) -> Unit) {
        this.observe(this@BaseActivity, Observer { data ->
            block(data)
        })
    }

}