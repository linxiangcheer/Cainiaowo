package com.test.service.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.test.service.R

/*
* 项目适配用的BindAdapter
* DataBinding扩展支持
* */

/*
* app:srcCompat的DataBinding扩展
* imageView支持图片加载 绑定
* */
@BindingAdapter("app:srcCompat", requireAll = false)
fun imgSrcCompat(iv: ImageView, src: Any?) {
    //如果src为空，设置默认图片
    val imgRes = src ?: R.drawable.icon_default_header
    Glide.with(iv)
        .load(imgRes)
        .into(iv)
}

/**
 * 图片资源支持tint颜色修改，支持colorRes和colorInt形式
 */
@BindingAdapter("app:tint")
fun imgColor(iv: ImageView, color: Int) {
    if (color == 0) return
    runCatching {
        iv.setColorFilter(iv.resources.getColor(color))
    }.onFailure {//捕获异常，说明此时是Color.RED形式 colorInt
        iv.setColorFilter(color)
    }
}

/**
 * textColor的binding形式，支持colorRes和colorInt形式
 */
@BindingAdapter("android:textColor")
fun tvColor(tv: TextView, color: Int) {
    if (color == 0) return
    runCatching {
        //如果是colorInt形式，会报错
        tv.setTextColor(tv.resources.getColor(color))
    }.onFailure {//捕获异常，说明此时是Color.RED形式
        tv.setTextColor(color)
    }
}

