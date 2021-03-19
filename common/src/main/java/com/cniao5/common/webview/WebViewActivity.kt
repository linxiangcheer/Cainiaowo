package com.cniao5.common.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.cniao5.common.R
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebView
import com.just.agentweb.BuildConfig
import com.just.agentweb.DefaultWebClient


/*
* webview初始化配置，需要在Manifest中声明activity和权限
* */
class WebViewActivity : AppCompatActivity() {

    private lateinit var mAgentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val llwebview = findViewById<LinearLayout>(R.id.ll_webview)

        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                llwebview,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator(resources.getColor(R.color.colorAccent))
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .get()

        val url = intent.getStringExtra("url")
        //        mAgentWeb.urlLoader.loadUrl("file:///android_asset/test.html")
        mAgentWeb.urlLoader.loadUrl(url)
        //添加js调用native的函数
        // mAgentWeb.jsInterfaceHolder.addJavaObject(JS_CALL_APP_KEY, JsAndroidApi)
        //开启webview的调试
        AgentWebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
    }

    //返回按钮事件
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /*
    * resum状态同步
    * */
    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    /*
    * Activity暂停状态，webview停止加载
    * */
    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    companion object {
        //传过来一个url，打开webviewactivity
        fun openUrl(context: Context, url: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).also {
                it.putExtra("url", url)
            })
        }
    }

}