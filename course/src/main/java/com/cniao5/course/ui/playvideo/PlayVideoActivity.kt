package com.cniao5.course.ui.playvideo

import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.cniao5.common.base.BaseActivity
import com.cniao5.course.R
import com.cniao5.course.databinding.FragmentPlayVideoBinding

class PlayVideoActivity: BaseActivity<FragmentPlayVideoBinding>() {

    override fun getLayoutRes() = R.layout.fragment_play_video

    override fun initConfig() {
        super.initConfig()

        val url = intent.getStringExtra("url")
        ToastUtils.showShort(url.toString())

        mBinding.jzVideo.setUp(url, "test test !!")
        //使用Gilde加载播放前显示的图片(推荐)
        Glide.with(this).load(R.drawable.img_course).into(mBinding.jzVideo.posterImageView)
        mBinding.jzVideo.startVideo()

    }

    override fun initData() {
        super.initData()

    }


companion object {
    //带参数context和url打开PlayVideoActivity
    fun openPlayVideo(context: Context, url: String) {
        context.startActivity(Intent(context, PlayVideoActivity::class.java).also {
            it.putExtra("url", url)
        })
    }
}




}