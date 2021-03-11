package com.cniao5.mine.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseFragment
import com.cniao5.common.network.config.SP_KEY_USER_TOKEN
import com.cniao5.common.utils.MySpUtils
import com.cniao5.mine.MineContainerFragment
import com.cniao5.mine.R
import com.cniao5.mine.databinding.FragmentMineBinding
import com.cniao5.mine.repo.MineRepo
import com.test.service.repo.DbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.notify
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
                // 更改为默认用户名
                // tvUserNameMine.text = "登录/免费注册"
                // //更改为默认头像
                // ivUserIconMine.setImageResource(R.drawable.icon_default_header)
                // 跳转到登录界面
                //todo 登录后再退出登录，此时点击界面上的返回按钮，viewmodel数据没有清除，图片和用户名都不会刷新
                ARouter.getInstance().build("/login/login").navigation()
            }

            //头像 跳转到 个人信息
            ivUserIconMine.setOnClickListener {
                //点击的时候赋值以免拿到空值
                val info = viewModel.liveInfo.value
                info?.let {
                    val action = MineFragmentDirections.actionMineFragmentToUserInfoFragment(info)
                    findNavController().navigate(action)
                }
            }

            //未登录状态下点击用户名位置跳转到登录界面
            tvUserNameMine.setOnClickListener {
                val info = viewModel.liveInfo.value
                ToastUtils.showShort("此时的username值为${info?.username}")
                if (info == null) ARouter.getInstance().build("/login/login").navigation()
            }

        }
    }

    override fun initData() {
        super.initData()
        //登录成功后会跳转到Mine这个界面，然后去获取数据库里的数据
        //requireContext 返回此片段的上下文
        //观察数据库的Userinfo,如果拿到userinfo获取用户个人信息，把token传进去
        DbHelper.getLiveUserInfo(requireContext()).observeKt {
            LogUtils.d("当前的token是${it?.token}")
            it?.let {
                viewModel.getUserInfo(it.token)
            }
        }
    }


}