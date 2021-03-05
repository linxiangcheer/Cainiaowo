package com.cniao5.common.model

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/*
* 但事件响应的liveData,只有一个接收者能接受到信息，可以避免不必要的业务场景中的事件消费通知
* 只有调用call的时候，observer才能收到通知
* */
class SingleLiveData <T>: MutableLiveData<T>() {

    // 原子变量
    // 在多线程环境下，当有多个线程同时执行这些类的实例包含的方法时，具有排他性
    // 即当某个线程进入方法，执行其中的指令时，不会被其他线程打断
    private val mPending = AtomicBoolean(false)

    @MainThread //主线程注解
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()){
            Log.w(TAG,"多个观察者存在的时候，只有一个会被通知到数据更新")
        }

        super.observe(owner, Observer { t ->
            // 比较AtomicBoolean和expect这两个值，如果一致，执行方法内的语句
            // 执行完之后把AtomicBoolean的值设为update的值
            // 任何内部或者外部的语句都不可能在两个动作之间运行
            if (mPending.compareAndSet(true, false))
                observer.onChanged(t)
        })
    }

    override fun setValue(value: T?) { //value可以为空不然会报错
        mPending.set(true)
        super.setValue(value)
    }

    /*
    * 传一些空值
    * */
    @MainThread //主线程注解
    fun call() {
        value = null
    }

    companion object{
        private const val TAG = "SingleLiveData"
    }

}