package com.cniao5.login

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    /*
    * ObservableField只有在数据发生改变时UI才会收到通知，而LiveData不同，只要你postValue或者setValue，UI都会收到通知，不管数据有无变化
    * livedata还能感知activity的生命周期，在Activity不活动的时候不会触发
    * */
    val obMobile = ObservableField<String>()
    val obPassword = ObservableField<String>()

    //登录按钮点击事件
    fun goLogin(){

    }

}