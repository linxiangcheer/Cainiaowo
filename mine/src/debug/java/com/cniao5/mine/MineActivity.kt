package com.cniao5.mine

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.ToastUtils
import com.cniao5.common.base.BaseActivity
import com.cniao5.mine.databinding.ActivityMineBinding
import com.cniao5.mine.widget.ItemSettingsBean

class MineActivity : BaseActivity<ActivityMineBinding>() {

    override fun getLayoutRes() = R.layout.activity_mine

    override fun initView() {
        super.initView()
        mBinding.apply {
            val ib = ItemSettingsBean(iconRes = R.drawable.ic_back, desc = "你好", title="学习卡")

            val obBean = ObservableField(ib)
            bean = obBean

            ib.title = "你的学习卡"
            ib.titleColor = Color.RED
            ib.iconRes = R.drawable.icon_server_host

            isvCard.onClickArrow {
                ToastUtils.showShort("点击箭头")
            }
            isvCard.setOnClickListener {
                ToastUtils.showShort("点击整个Item")
            }
        }
    }
}