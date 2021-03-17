package com.cniao5.home.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.cniao5.common.base.BaseFragment
import com.cniao5.home.R
import com.cniao5.home.databinding.FragmentHomeBinding
import com.cniao5.home.net.BannerList
import com.cniao5.home.ui.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
* 主页的Fragment
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class HomeFragment: BaseFragment() {

    private val viewmodel: HomeViewModel by viewModel()

    private val bannerList = BannerList()

    private val bannerAdapter by lazy { BannerAdapter(bannerList) }

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_home

    //传入view,将view和databinding绑定到一起
    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        return FragmentHomeBinding.bind(view).apply {

            bannerHome.addBannerLifecycleObserver(viewLifecycleOwner) //生命周期观察者
                .setAdapter(bannerAdapter).indicator = CircleIndicator(requireContext()) //轮播图上的小点
        }
    }

    override fun initConfig() {
        super.initConfig()

    }

    override fun initData() {
        super.initData()

        //上方banner
        viewmodel.getBanner()
        viewmodel.liveBanner.observeKt {
            it ?: return@observeKt
            bannerList.clear()
            bannerList.addAll(it) //todo 添加多一次，因为返回的只有一个数据
            bannerList.addAll(it)
            bannerAdapter.notifyDataSetChanged()
        }


    }
}