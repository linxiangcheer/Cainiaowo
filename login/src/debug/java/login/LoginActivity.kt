package login

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseActivity
import login.databinding.ActivityLoginBinding
import login.net.RegisterRsp
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModel()

    override fun getLayoutRes() = R.layout.activity_login

    override fun initView() {
        super.initView()
        mBinding.apply {
            vm = viewModel
            //左上角退出按钮点击事件
            mtoolbarLogin.setNavigationOnClickListener { finish() }
            //注册新账号按钮点击事件
            tvRegisterLogin.setOnClickListener {
                ToastUtils.showShort("当前课程项目为实现注册账号功能")
            }

        }

    }

    override fun initConfig() {
        super.initConfig()

        //在UI里观察两个请求的返回结果
        viewModel.apply {
            liveRegisterRsp.observerKt {
                if (it.is_register == RegisterRsp.FLAG_IS_REGISTERED) {
                    repoLogin() //登录请求
                }
            }
            liveLoginRsp.observerKt {
                LogUtils.i("登录结果:$it")
            }
        }

    }

    override fun initData() {
        super.initData()
    }

}