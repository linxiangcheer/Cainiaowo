package login

import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseActivity
import com.cniao5.common.ktx.context
import com.test.service.repo.DbHelper
import com.test.service.repo.UserInfo
import login.databinding.ActivityLoginBinding
import login.net.LoginRsp
import login.net.RegisterRsp
import org.koin.androidx.viewmodel.ext.android.viewModel

//路由地址
@Route(path = "/login/login")
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
                LogUtils.i("登录结果:${it}")

                //将数据保存到数据库里
                DbHelper.insertUserInfo(context, UserInfo(1, LoginRsp(it.id, it.isBindPhone, it.logo_url, it.token, it.username)))
                //关闭Activity
                finish()
            }
        }

    }


}