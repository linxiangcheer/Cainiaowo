package com.cniao5.common.network.support

import androidx.lifecycle.LiveData
import com.cniao5.common.model.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *  用于将retrofit的返回数据，转化为liveData的adapter的工厂类
 */
class LiveDataCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }
        val observableType: Type = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType: Class<*> = getRawType(observableType)
        if (rawObservableType != ApiResponse::class.java) {
            throw IllegalArgumentException("type must be a resource")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be a parameterized")
        }
        val bodyType: Type = getParameterUpperBound(0, observableType)
        return LiveDataCallAdapter<Any>(bodyType)
    }
}