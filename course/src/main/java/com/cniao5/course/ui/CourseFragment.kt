package com.cniao5.course.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseFragment
import com.cniao5.course.CourseViewModel
import com.cniao5.course.R
import com.cniao5.course.databinding.*
import com.cniao5.course.ui.playvideo.PlayVideoActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
* 课程的Fragment
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class CourseFragment: BaseFragment() {

    private val viewModel: CourseViewModel by viewModel()
    private lateinit var mBinding: FragmentCourseBinding

    //传url并跳转到playvideoactivity
    private val coursePagingAdapter = CoursePagingAdapter {
        PlayVideoActivity.openPlayVideo(requireContext(), "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", it) //todo测试url
        // PlayVideoActivity.openPlayVideo(requireContext(), it.imgUrl)
    }

    //初始化为all,点击上方tabitem之后记得重新赋值
    private var code = "all"     //方向从课程分类接口获取    默认 all;例如 android,python
    private var difficulty = -1     //难度 (-1 全部) (1 初级) (2 中级) (3 高级) (4 架构) 默认 -1
    private var is_free = -1     //价格 (-1, 全部) （0 付费） (1 免费) 默认 -1
    private var q = -1   //排序  (-1 最新) (1 评价最高)  (2 学习最多) 默认 -1
    /*
    * 默认code = all
    * "code": "actual_combat","title": "实战"
      "code": "bd","title": "大数据"
      "code": "android","title": "Android"
      "code": "python",title": "Python"
      "code": "java"，"title": "Java"
    * */

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_course

    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        mBinding = FragmentCourseBinding.bind(view)

        return mBinding.apply {

            vm = viewModel

            val adapter = coursePagingAdapter
            //下方的load Header头部load
            rvCourse.adapter = adapter.withLoadStateFooter(CourseLoadAdapter(adapter))

            //Load上滑进度条监听
            adapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    pbFragmentCourse.visibility = View.VISIBLE
                } else {
                    pbFragmentCourse.visibility = View.GONE

                    //获取加载错误事件
                    val error = when {
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }

                    error?.let {
                        ToastUtils.showShort(it.error.message)
                    }
                }
            }

            viewModel.apply {

                //课程分类观察者
                liveCourseType.observeKt { types ->
                    //清除所有的tabitem 因为返回的数据没有"全部"，要添加上，以防刷新丢掉了"全部"
                    tlCategoryCourse.removeAllTabs()
                    val tab = tlCategoryCourse.newTab().also { tab ->
                        tab.text = "全部"
                    }
                    tlCategoryCourse.addTab(tab)
                    //对每个元素都执行指定的操作
                    types?.forEach { item ->
                        tlCategoryCourse.addTab(
                            tlCategoryCourse.newTab().also { tab ->
                                tab.text = item.title
                            }
                        )
                    }
                }

                //进度条显示
                isLoading.observe(viewLifecycleOwner) {
                    //协程block获取数据代码块是否结束，协程结束时为false
                    pbFragmentCourse.visibility = if (it) View.VISIBLE else View.GONE
                }

                /*
                * tablayout点击事件
                * 打开的时候会点击一次
                * */
                tlCategoryCourse.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    //tab被点击的时候回调 进入界面触发all
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        //拿到获取的CourseType.value
                        val courseTypes = liveCourseType.value
                        //拿到点击的tab的position
                        val index = tab?.position ?: 0
                        code = if (index > 0) { //根据tab的选择改变code的值
                            /*
                            * 界面上第一个是全部,position = 0
                            * 第二个是 xx,position = 1, 但是在数据源中是0
                            * */
                            courseTypes?.get(index -1)?.code ?: "all"
                        } else "all"
                        refreshData()
                    }

                    //tab未被点击时回调
                    override fun onTabUnselected(tab: TabLayout.Tab?) {   }

                    //tab重新点击时回调
                    override fun onTabReselected(tab: TabLayout.Tab?) {   }
                })

                /*
                * 课程列表观察者
                * */
                liveCourseListRsp.observeKt {
                    courseRecycAdapter.submitClear(it?.datas ?: emptyList())
                }

                //全部类型按钮点击事件
                spinnerTypeCourse.setOnClickListener {
                    popupWindow_type.showAsDropDown(it)
                }

                //全部难度按钮点击事件
                spinnerLevelCourse.setOnClickListener {
                    popupWindow_level.showAsDropDown(it)
                }

                //全部价格按钮点击事件
                spinnerPriceCourse.setOnClickListener {
                    popupWindow_price.showAsDropDown(it)
                }

                //默认排序按钮点击事件
                spinnerSortCourse.setOnClickListener {
                    popupWindow_sort.showAsDropDown(it)
                }

            }

        }
    }

    override fun initConfig() {
        super.initConfig()
        viewModel.getCourseCategory()
        configPopFilter() //弹窗
    }

    //popupWindow 在activity最上层显示一个弹窗
    private lateinit var popupWindow_type: PopupWindow //全部类型
    private lateinit var popupWindow_level: PopupWindow //全部难度
    private lateinit var popupWindow_price: PopupWindow //全部价格
    private lateinit var popupWindow_sort: PopupWindow //默认排序
    //popupwindow初始化和点击事件处理
    private fun configPopFilter() {

        //region 全部类型弹窗
        val popBinding = PopCourseTypeBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        popupWindow_type = PopupWindow(
            popBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        //点击外部消失和穿透事件处理
        outSideTouch(popupWindow_type)

        //一个有初始值的observable，用来显示弹窗文字是否被点击和右边的勾显示与否，被点击set值，控件获取到对应的值后改变文字颜色
        val obCheckPos = ObservableInt(0)

        popBinding.apply {
            pos = obCheckPos
            tvAllType.setOnClickListener {
                obCheckPos.set(0)
                mBinding.spinnerTypeCourse.text = "全部类型"
                popupWindow_type.dismiss() //解除popup
                refreshData()
            }
            tvBizType.setOnClickListener {
                obCheckPos.set(1)
                mBinding.spinnerTypeCourse.text = "商业实战"
                popupWindow_type.dismiss()
                refreshData()
            }
            tvProType.setOnClickListener {
                obCheckPos.set(2)
                mBinding.spinnerTypeCourse.text = "专业好课"
                popupWindow_type.dismiss()
                refreshData()
            }
        }
        // endregion

        //region 全部难度弹窗
        val popBinding1 = PopCourseLevelBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        popupWindow_level = PopupWindow(
            popBinding1.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        //点击外部消失和穿透事件处理
        outSideTouch(popupWindow_level)

        //一个有初始值的observable，用来显示弹窗文字是否被点击和右边的勾显示与否，被点击set值，控件获取到对应的值后改变文字颜色
        val obCheckPos1 = ObservableInt(0)

        popBinding1.apply {
            pos = obCheckPos1
            tvAll.setOnClickListener {
                obCheckPos1.set(0)
                mBinding.spinnerLevelCourse.text = "全部难度"
                difficulty = -1
                popupWindow_level.dismiss() //解除popup
                refreshData()
            }
            tvTwo.setOnClickListener {
                obCheckPos1.set(1)
                mBinding.spinnerLevelCourse.text = "初级"
                difficulty = 1
                popupWindow_level.dismiss() //解除popup
                refreshData()
            }
            tvThree.setOnClickListener {
                obCheckPos1.set(2)
                mBinding.spinnerLevelCourse.text = "中级"
                difficulty =2
                popupWindow_level.dismiss() //解除popup
                refreshData()
            }
            tvFour.setOnClickListener {
                obCheckPos1.set(3)
                mBinding.spinnerLevelCourse.text = "高级"
                difficulty = 3
                popupWindow_level.dismiss() //解除popup
                refreshData()
            }
            tvFive.setOnClickListener {
                obCheckPos1.set(4)
                mBinding.spinnerLevelCourse.text = "架构"
                difficulty = 4
                popupWindow_level.dismiss() //解除popup
                refreshData()
            }
        }
        //endregion

        //region 全部价格弹窗
        val popBinding2 = PopCoursePriceBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        popupWindow_price = PopupWindow(
            popBinding2.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        //点击外部消失和穿透事件处理
        outSideTouch(popupWindow_price)

        //一个有初始值的observable，用来显示弹窗文字是否被点击和右边的勾显示与否，被点击set值，控件获取到对应的值后改变文字颜色
        val obCheckPos2 = ObservableInt(0)

        popBinding2.apply {
            pos = obCheckPos2
            tvAll.setOnClickListener {
                obCheckPos2.set(0)
                mBinding.spinnerPriceCourse.text = "全部价格"
                is_free = -1
                popupWindow_price.dismiss() //解除popup
                refreshData()
            }
            tvTwo.setOnClickListener {
                obCheckPos2.set(1)
                mBinding.spinnerPriceCourse.text = "免费"
                is_free = 1
                popupWindow_price.dismiss() //解除popup
                refreshData()
            }
            tvThree.setOnClickListener {
                obCheckPos2.set(2)
                mBinding.spinnerPriceCourse.text = "付费"
                is_free = 0
                popupWindow_price.dismiss() //解除popup
                refreshData()
            }
        }
        //endregion

        //region 默认排序弹窗
        val popBinding3 = PopCourseSortBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        popupWindow_sort = PopupWindow(
            popBinding3.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        //点击外部消失和穿透事件处理
        outSideTouch(popupWindow_sort)

        //一个有初始值的observable，用来显示弹窗文字是否被点击和右边的勾显示与否，被点击set值，控件获取到对应的值后改变文字颜色
        val obCheckPos3 = ObservableInt(0)

        popBinding3.apply {
            pos = obCheckPos3
            tvAll.setOnClickListener {
                obCheckPos3.set(0)
                mBinding.spinnerSortCourse.text = "默认排序"
                q = -1
                popupWindow_sort.dismiss() //解除popup
                refreshData()
            }
            tvTwo.setOnClickListener {
                obCheckPos3.set(1)
                mBinding.spinnerSortCourse.text = "评价最高"
                q = 0
                popupWindow_sort.dismiss() //解除popup
                refreshData()
            }
            tvThree.setOnClickListener {
                obCheckPos3.set(2)
                mBinding.spinnerSortCourse.text = "学习最多"
                q = 1
                popupWindow_sort.dismiss() //解除popup
                refreshData()
            }
        }
        //endregion

    }

    //请求数据
    private fun refreshData(courseType: Int = -1) {
        // viewModel.getCourseList(code, difficulty, is_free,q)
        lifecycleScope.launchWhenCreated {
            viewModel.getCourseListPaging(code = code, difficulty, is_free, q).collectLatest {
                coursePagingAdapter.submitData(it)
            }
        }
    }

    /*
    * PopupWindow outsidetouchable和点击穿透事件处理
    * */
    private fun outSideTouch(popupwindow: PopupWindow) {
        //弹窗背景（大部分被控件遮挡）
        popupwindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupwindow.isOutsideTouchable = true //点击外部消失
        popupwindow.isFocusable = true  //focusable容易忽略 防止点击事件穿透
    }


}