package com.cniao5.course.ui.playvideo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.cniao5.common.base.BaseActivity
import com.cniao5.course.PlayVideoViewModel
import com.cniao5.course.R
import com.cniao5.course.databinding.FragmentPlayVideoBinding
import com.cniao5.course.net.CourseDetails
import com.cniao5.course.net.CourseListRsp
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayVideoActivity : BaseActivity<FragmentPlayVideoBinding>() {

    private val viewmodel: PlayVideoViewModel by viewModel()

    override fun getLayoutRes() = R.layout.fragment_play_video

    override fun initConfig() {
        super.initConfig()

        val url = intent.getStringExtra("url")
        val info = intent.getParcelableExtra<CourseListRsp.Data>("info")

        info?.let {
            mBinding.info = info
            mBinding.clCardCourse.tvOldPriceItemCourse.paint.flags =
                Paint.STRIKE_THRU_TEXT_FLAG //实现删除线（中划线）效果

            //请求播放列表数据
            viewmodel.getCourseDetails(it.id)
        }
        mBinding.vm = viewmodel



        // ToastUtils.showShort(url.toString())

        //是否保存播放进度
        Jzvd.SAVE_PROGRESS = false

        mBinding.jzVideo.setUp(url, "test test !!")
        //使用Gilde加载播放前显示的图片(推荐)
        Glide.with(this).load(R.drawable.img_course).into(mBinding.jzVideo.posterImageView)
        mBinding.jzVideo.startVideo()

        //点击某个分组监听事件
        mBinding.listview.setOnGroupClickListener { parent, v, groupPosition, id ->
            ToastUtils.showShort("你点击了${viewmodel.arrayLiveCourseDetails[groupPosition]?.title}")
            false //true 屏蔽一级列表点击
        }

        //分组中的子view点击事件
        mBinding.listview.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            ToastUtils.showShort("你点击了${viewmodel.arrayLiveCourseDetails[groupPosition]?.lessons?.get(childPosition)?.name},")
            false
        }


    }

    override fun initData() {
        super.initData()

        //课程播放目录列表
        viewmodel.liveCourseDetails.observerKt { courseDetail ->

            courseDetail?.addAll(courseDetail) //todo 手动添加多一串数据

            courseDetail?.let{viewmodel.arrayLiveCourseDetails = courseDetail} //把数据存储到viewmodel

            mBinding.listview.setAdapter(courseDetail?.let { ChapterAdapter(it) })
        }

    }

    companion object {
        //带参数context和url打开PlayVideoActivity
        fun openPlayVideo(context: Context, url: String, info: CourseListRsp.Data) {
            context.startActivity(Intent(context, PlayVideoActivity::class.java).also {
                it.putExtra("url", url)
                it.putExtra("info", info) //界面include的数据
            })
        }
    }

    override fun onPause() {
        super.onPause()
        //取消所有播放
        Jzvd.releaseAllVideos()
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }


}