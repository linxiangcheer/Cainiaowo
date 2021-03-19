package com.cniao5.home.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.cniao5.common.base.BaseFragment
import com.cniao5.common.model.DataResult
import com.cniao5.home.R
import com.cniao5.home.databinding.FragmentHomeBinding
import com.cniao5.home.net.*
import com.cniao5.home.ui.adapter.BannerAdapter
import com.cniao5.home.ui.adapter.HomeAdapter
import com.test.service.net.*
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
* 主页的Fragment
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class HomeFragment : BaseFragment() {

    private val adapter: HomeAdapter = HomeAdapter()

    private val viewmodel: HomeViewModel by viewModel()

    private val bannerList = BannerList()

    private val bannerAdapter by lazy { BannerAdapter(bannerList) }

    private val homeList = arrayListOf<HomeData>() //真正的homeList详细数据 type title data

    private lateinit var mbinding: FragmentHomeBinding

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_home

    //传入view,将view和databinding绑定到一起
    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        mbinding = FragmentHomeBinding.bind(view)
        return mbinding.apply {

            //adapter传给databinding，可以直接绑定
            adapter = this@HomeFragment.adapter

            bannerHome.addBannerLifecycleObserver(viewLifecycleOwner) //生命周期观察者
                .setAdapter(bannerAdapter).indicator = CircleIndicator(requireContext()) //轮播图上的小点
        }
    }

    override fun initConfig() {
        super.initConfig()
        viewmodel.apply {
            //上方banner
            getBanner()
            //模块名字、请求地址id url
            getHomeList()
        }
    }

    override fun initData() {
        super.initData()

        viewmodel.apply {

            //加载进度条显示
            isLoading.observe(viewLifecycleOwner) {
                //协程block获取数据代码块是否结束，协程结束时为false
                mbinding.pbFragmentCourse.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }

            liveBanner.observeKt {
                it ?: return@observeKt

                bannerList.clear()
                bannerList.addAll(it) //todo 添加多两次，因为返回的只有一个数据
                bannerList.addAll(it)
                bannerList.addAll(it)
                bannerAdapter.notifyDataSetChanged()
            }

            val scope = CoroutineScope(SupervisorJob()) //全部完成才会报成功，只要有一个失败就返回失败
            liveHomeList.observeKt { homedatas ->
                // Log.d("yyy", homedatas.toString())
                homedatas ?: return@observeKt //如果返回的是空就结束

                // Log.d("yyy","liveHomeList请求数据")

                lifecycleScope.launchWhenCreated {
                    //返回一个列表，其中包含对原始集合中的每个元素应用给定转换函数的结果
                    homedatas.map { item ->
                        Triple( //三位一体的值
                            item.id,
                            item.title,
                            scope.async { getModuleType(item.type) } //getModuleType中传入item.type来判断获取哪种类型的数据,一般开发下是传入唯一id,各自有各自的接口
                                .await() //等待，不结束之前不会获取下一个数据
                        )
                    }.asFlow().collect {
                        it.third?.let { it3 -> parseData(it.first, it.second, it3)
                        }
                    }
                    //排序，把金牌讲师放到最后
                    adapter.upRecyclerViewList(putTeacherLast(homeList)) //把数据传给HomeAdapter适配器,让它去分配数据
                }
            }
        }
    }

    /*
    * 传入唯一的id来识别是哪个标签的数据
    * */
    private fun parseData(id: Int, title: String?, data: DataResult<BaseResponse>) {
        //dataSuccess中的数据
        data.onSuccess {
            when (id) {
                ID_JOB_CLASS -> { //班级
                    onBizOK<JobClassList> { code, data, message ->
                        homeList.add(HomeData(id, title, data))
                    }
                    onBizError { code, message ->
                        LogUtils.w("获取首页列表详情信息 onBizError $code,$message")
                    }
                }
                ID_NEW_CLASS -> { //新上好课
                    onBizOK<NewClassList> { code, data, message ->
                        homeList.add(HomeData(id, title, data))
                    }
                    onBizError { code, message ->
                        LogUtils.w("获取首页列表详情信息 onBizError $code,$message")
                    }
                }
                ID_COMBIT -> { //实战推荐
                    onBizOK<CombatList> { code, data, message ->
                        homeList.add(HomeData(id, title, data))
                    }
                    onBizError { code, message ->
                        LogUtils.w("获取首页列表详情信息 onBizError $code,$message")
                    }
                }
                ID_LIMIT_FREE -> { //免费好课
                    onBizOK<LimitFreeList> { code, data, message ->
                        homeList.add(HomeData(id, title, data))
                    }
                    onBizError { code, message ->
                        LogUtils.w("获取首页列表详情信息 onBizError $code,$message")
                    }
                }
                ID_POP_TEACHER -> { //金牌讲师
                    onBizOK<TeacherList> { code, data, message ->
                        homeList.add(HomeData(id, title, data))
                    }
                    onBizError { code, message ->
                        LogUtils.w("获取首页列表详情信息 onBizError $code,$message")
                    }
                }
                else -> {  }
            }
        }
            .onFailure {
                LogUtils.e("获取首页列表详情信息 接口异常 $it")
            }

    }

    //重排序 把"金牌讲师"放在最后  只在flag = false的之后执行
    fun putTeacherLast(list: ArrayList<HomeData>) : ArrayList<HomeData>{
        var teacherdata: HomeData? = null
            for (i in 0 until list.size -1) {
                if (list[i].title.equals("金牌讲师") && i != list.size - 1) {
                    teacherdata = list[i]
                    list.removeAt(i)
                    teacherdata.let { list.add(list.size, it) }
                }
        }
        return list
    }

    // private val TAG = "yyy"
    //
    // override fun onAttach(context: Context) {
    //     super.onAttach(context)
    //     Log.d(TAG, "onAttach: ")
    // }
    //
    // override fun onCreate(savedInstanceState: Bundle?) {
    //     super.onCreate(savedInstanceState)
    //     Log.d(TAG, "onCreate: ")
    // }
    //
    // override fun onDestroyView() {
    //     super.onDestroyView()
    //     Log.d(TAG, "onDestroyView: ")
    // }

    //activity被销毁的时候清除数据
    override fun onDetach() {
        super.onDetach()
        homeList.clear() //清空list的数据
        viewmodel.liveBanner.value?.clear() //清空liveBanner的数据
        viewmodel.liveHomeList.value?.clear() //清空liveHomeList的数据
        adapter.clearRecyclerViewList() //清空recyclerview的数据
    }


    // override fun onDestroy() {
    //     super.onDestroy()
    //     Log.d(TAG,"onDestroy")
    // }
    //
    // override fun onStart() {
    //     super.onStart()
    //     Log.d(TAG,"onstart")
    // }
    //
    // override fun onPause() {
    //     super.onPause()
    //     Log.d(TAG,"onPause")
    // }
    //
    // override fun onStop() {
    //     super.onStop()
    //     Log.d(TAG,"onstop")
    //     // viewmodel.liveHomeList.value?.clear()
    // }
    //
    // override fun onResume() {
    //     super.onResume()
    //     Log.d(TAG,"onresume")
    // }

    companion object {

        internal const val ID_JOB_CLASS = 7 //即将开班
        internal const val ID_NEW_CLASS = 1 //新上好课
        internal const val ID_COMBIT = 26 //实战推荐
        internal const val ID_LIMIT_FREE = 2 //免费好课
        internal const val ID_POP_TEACHER = 4 //金牌讲师

    }

    data class HomeData(
        val id: Int, //id
        val title: String?, //tab名
        val obj: Any? //解析出来的对应模块的data数据
    )

}
