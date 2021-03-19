package com.cniao5.mine.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseFragment
import com.cniao5.common.network.config.SP_KEY_USER_TOKEN
import com.cniao5.common.utils.MySpUtils
import com.cniao5.common.webview.WebViewActivity
import com.cniao5.mine.MineViewModel
import com.cniao5.mine.R
import com.cniao5.mine.databinding.FragmentMineBinding
import com.cniao5.mine.repo.UserInfoRspDBHelper
import com.test.service.repo.DbHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
* 我的界面 Fragment
* 传入了R.layout.fragment_course之后就不用写onCreateView,因为布局已经被关联到fragment里了
* */
class MineFragment : BaseFragment() {

    private val viewModel: MineViewModel by viewModel()

    //传入布局资源,将布局和view绑定到一起
    override fun getLayoutRes() = R.layout.fragment_mine

    //传入view,将view和databinding绑定到一起
    override fun bindView(view: View, savedInstanceState: Bundle?): ViewDataBinding {
        return FragmentMineBinding.bind(view).apply {
            vm = viewModel
            //UIcaozuo 登出
            btnLogoutMine.setOnClickListener {
                //清空登录数据
                DbHelper.deleteUserInfo(requireContext())
                //清空存储在本地的token
                MySpUtils.remove(SP_KEY_USER_TOKEN)
                // 删除个人信息表里的数据
                UserInfoRspDBHelper.deleteUserInfoRsp(requireContext())
                //跳转到Login登录界面
                ARouter.getInstance().build("/login/login").navigation()
            }

            //头像 跳转到 个人信息
            ivUserIconMine.setOnClickListener {
                //点击的时候赋值以免拿到空值
                val info = viewModel.liveInfoRsp.value
                info?.let {
                    val action = MineFragmentDirections.actionMineFragmentToUserInfoFragment(info)
                    findNavController().navigate(action)
                }
            }

            //未登录状态下点击用户名位置跳转到登录界面
            tvUserNameMine.setOnClickListener {
                val info = viewModel.liveInfoRsp.value
                // ToastUtils.showShort("此时的username值为${info?.username}")
                if (info == null) ARouter.getInstance().build("/login/login").navigation()
            }
            //各个按钮的点击事件，跳转到H5
            tvOrdersMine.setOnClickListener {
                ToastUtils.showShort("正在访问正式接口")
                WebViewActivity.openUrl(requireContext(), "https://m.cniao5.com/user/order")
            }
            tvCouponMine.setOnClickListener {
                ToastUtils.showShort("正在访问正式接口")
                WebViewActivity.openUrl(requireContext(), "https://m.cniao5.com/user/coupon")
            }
            isvStudyCardMine.setOnClickListener {
                ToastUtils.showShort("正在访问正式接口")
                WebViewActivity.openUrl(requireContext(), "https://m.cniao5.com/sharecard")
            }
            isvShareSaleMine.setOnClickListener {
                ToastUtils.showShort("正在访问正式接口")
                WebViewActivity.openUrl(requireContext(), "https://m.cniao5.com/distribution")
            }
            isvGroupShoppingMine.setOnClickListener {
                ToastUtils.showShort("正在访问正式接口")
                WebViewActivity.openUrl(requireContext(), "https://m.cniao5.com/user/pintuan")
            }
            isvLikedCourseMine.setOnClickListener {
                ToastUtils.showShort("正在访问正式接口")
                WebViewActivity.openUrl(requireContext(), "https://m.cniao5.com/user/favorites")
            }
            isvFeedbackMine.setOnClickListener {
                ToastUtils.showShort("正在访问正式接口")
                //8.0以后不建议用http,需要在app manifest中声明 android:usesCleartextTraffic="true"
                WebViewActivity.openUrl(requireContext(), "http://cniao555.mikecrm.com/ktbB0ht")
            }

        }
    }

    override fun initData() {
        super.initData()
        //登录成功后会跳转到Mine这个界面，然后去获取数据库里的数据
        //requireContext 返回此片段的上下文
        //观察数据库的Userinfo,如果拿到userinfo获取用户个人信息，把token传进去

        //此时观察的只是userinfo账号登录信息
        DbHelper.getLiveUserInfo(requireContext()).observeKt {
            // LogUtils.d("当前的token是否存在:${it?.token}")
            it?.let {
                viewModel.getUserInfo(it.token)
            }
        }

        //观察个人信息表
        UserInfoRspDBHelper.getLiveUserInfoRsp(requireContext()).observeKt { info ->
            // LogUtils.d("个人信息表是否存在:${info?.username}")
            info?:let { //如果这个表为空就执行
                //清空liveInfoRsp的数据，此时info=null
                viewModel.liveInfoRsp.value = info
            }
        }

        viewModel.apply {
            //如果Liveinfo数据更新不为空的话就存储到数据库中
            liveInforep.observeKt {
                it?.let {
                    UserInfoRspDBHelper.insertUserInfoRsp(requireContext(), it)
                    //将数据给到布局的livedata
                    liveInfoRsp.value = it
                }
            }
        }

    }


}