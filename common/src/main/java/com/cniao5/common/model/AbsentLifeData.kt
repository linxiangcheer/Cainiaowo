package com.cniao5.common.model

import androidx.lifecycle.LiveData

/*
* 创建一个空的liveData的对象类
* 常用于创建一个空的liveData对象来转接数据
* */
class AbsentLifeData<T: Any?> private constructor(): LiveData<T>() {

    init {
        postValue(null)
    }

    companion object {
        fun <T: Any?> create(): LiveData<T> {
            return AbsentLifeData()
        }
    }

}