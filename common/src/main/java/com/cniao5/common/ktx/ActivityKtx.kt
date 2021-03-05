package com.cniao5.common.ktx

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner

/*
 * Activity相关的ktx，扩展函数或扩展属性
 * */

/*
 * Activity中使用DataBinding时setContentView的简化
 * layout 布局文件
 * return 返回一个Binding的对象实例
 * @LayoutRes 可以检测是否是资源布局
 * */
fun <T : ViewDataBinding> Activity.bindView(@LayoutRes layout: Int): T {
    return DataBindingUtil.setContentView(this, layout)
}

/*
 * Activity中使用DataBinding时setContentView的简化
 * layout 布局文件
 * return 返回一个Binding的对象实例 T类型的 可null的
 * */
fun <T : ViewDataBinding> Activity.bindView(view: View): T? {
    return DataBindingUtil.bind<T>(view)
}

/*
* 界面Activity的沉浸式状态栏，使得可以在状态栏里面显示部分需要的图片
* 注意：需要在setContentView之前调用该函数才生效
* */
fun Activity.immediateStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.let {
            it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        @Suppress("DEPRECATION")
        //            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
        //                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        //                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }
}

/*
* 软键盘的隐藏
* view 事件控件view
* */
fun Activity.dismissKeyBoard(view: View){
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

    //region 扩展属性
    /*
    * 扩展lifeCycleOwner属性，便于和Fragment之间使用lifecycleOwner的时候保持参数一致性
    * */
    val ComponentActivity.viewLifeCycleOwner: LifecycleOwner
    get() = this

    /*
    * Activity的扩展字段，便于Fragment中使用livedata之类的时候，参数一致性
    * */
    val Activity.context: Context
    get() = this
//endregion
