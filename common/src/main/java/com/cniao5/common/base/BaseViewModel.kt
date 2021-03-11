package com.cniao5.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/*
* viewModel的公共基类
* */
abstract class BaseViewModel : ViewModel() {
    //job的列表
    private val jobs: MutableList<Job> = mutableListOf<Job>()
    val isLoading = MutableLiveData<Boolean>() //标记网络loading状态

    /**
     * 启用协程代码块 网络请求
     * 用protected权限修饰，只能子类实现
     */
    protected fun serverAwait(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        isLoading.value = true //协程启动之前
        block.invoke(this)
        isLoading.value = false //协程启动之后
    }.addTo(jobs)

    override fun onCleared() {
        jobs.forEach { it.cancel() }
        super.onCleared()
    }
}

/**
 * 扩展函数，用于viewModel中的job添加到list方便
 */
private fun Job.addTo(list: MutableList<Job>) {
    list.add(this)
}