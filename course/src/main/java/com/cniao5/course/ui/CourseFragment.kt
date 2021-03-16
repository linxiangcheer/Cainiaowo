package com.cniao5.course.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TableLayout
import android.widget.Toast
import androidx.databinding.Observable
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseFragment
import com.cniao5.course.R
import com.cniao5.course.databinding.FragmentCourseBinding
import com.cniao5.course.databinding.PopCourseTypeBinding
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
* 课程的Fragment
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class CourseFragment: BaseFragment() {

    private val viewModel: CourseViewModel by viewModel()
    private lateinit var mBinding: FragmentCourseBinding

    private var code = "all"

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_course

    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        mBinding = FragmentCourseBinding.bind(view)
        return mBinding.apply {

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
                * */
                tlCategoryCourse.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    //tab被点击的时候回调 进入界面触发all
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        //拿到获取的CourseType.value
                        val courseTypes = liveCourseType.value
                        //拿到点击的tab的position
                        val index = tab?.position ?: 0
                        code = if (index > 0) {
                            /*
                            * 界面上第一个是全部,position = 0
                            * 第二个是 xx,position = 1, 但是在数据源中是0
                            * */
                            courseTypes?.get(index -1)?.code ?: "all"
                        } else "all"
                        ToastUtils.showShort("点击$code")
                        //todo 请求逻辑
                    }

                    //tab未被点击时回调
                    override fun onTabUnselected(tab: TabLayout.Tab?) {   }

                    //tab重新点击时回调
                    override fun onTabReselected(tab: TabLayout.Tab?) {   }
                })

                //全部类型按钮点击事件
                spinnerTypeCourse.setOnClickListener {
                    //todo popWindow
                    popWindow.showAsDropDown(it)
                }

            }

        }
    }

    override fun initConfig() {
        super.initConfig()
        viewModel.getCourseCategory()
        configPopFilter() //弹窗初始化
    }

    //popupWindow 在activity最上层显示一个弹窗
    private lateinit var popWindow: PopupWindow
    //popupwindow初始化
    private fun configPopFilter() {
        val popBinding = PopCourseTypeBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        popWindow = PopupWindow(
            popBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val obCheckPos = ObservableInt(0)

        popBinding.apply {
            pos = obCheckPos
            tvAllType.setOnClickListener {
                obCheckPos.set(0)
                popWindow.dismiss()
                refreshData(code)
            }
            tvBizType.setOnClickListener {
                obCheckPos.set(1)
                popWindow.dismiss()
                refreshData(code, 1)
            }
            tvProType.setOnClickListener {
                obCheckPos.set(2)
                popWindow.dismiss()
                refreshData(code, 2)
            }
        }
    }

    private fun refreshData(code: String, courseType: Int = -1) {
        //todo 隐藏图片,最上层textview显示点击到的那一栏,重复点击消失，点击弹窗外边弹窗消失
        ToastUtils.showShort("刷新数据")
    }


}