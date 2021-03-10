package com.cniao5.mine.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Keep
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ObservableField
import com.cniao5.mine.R
import com.cniao5.mine.databinding.VItemSettingsBinding


/*
* 自定义的设置item的组合控件
* */

/*
* @JvmOverloads可以把多个构造函数合并成一个构造函数
* @JvmOverloads，为其注解的方法替换默认参数生成重载函数
* 如果一个方法有N个参数且其中M个有默认值，则会产生M个重载
* */
class ItemSettingsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private var itemBean = ItemSettingsBean()
    private val obItemInfo = ObservableField<ItemSettingsBean>(itemBean)

    //必然会走的代码块
    init {
        //自定义控件要附着要attcah上  管理布局layout 返回的是一个BINDING对象
        //1、绑定view
        VItemSettingsBinding.inflate(LayoutInflater.from(context), this, true).apply {
            info = obItemInfo
        }
        setBackgroundColor(Color.WHITE)

        // region  2、读取配置属性，才能对外使用Databinding
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ItemSettingsView).apply {
            //icon设置
            itemBean.iconRes =
                getResourceId(R.styleable.ItemSettingsView_icon, R.drawable.icon_server_host)
            val iconRGB = getColor(R.styleable.ItemSettingsView_iconColor, 0)
            itemBean.iconColor = iconRGB
            //title设置
            itemBean.title = getString(R.styleable.ItemSettingsView_title) ?: ""
            val titleRGB = getColor(
                R.styleable.ItemSettingsView_titleColor,
                resources.getColor(R.color.colorPrimaryText)
            )
            itemBean.titleColor = titleRGB
            //desc设置
            itemBean.desc = getString(R.styleable.ItemSettingsView_desc) ?: ""
            val descRGB = getColor(R.styleable.ItemSettingsView_descColor, 0)
            itemBean.descColor = descRGB
            //arrow设置
            itemBean.arrowRes =
                getResourceId(R.styleable.ItemSettingsView_arrow, R.drawable.ic_right)
            val arrowRGB = getColor(
                R.styleable.ItemSettingsView_arrowColor,
                resources.getColor(R.color.colorSecondaryText)
            )
            itemBean.arrowColor = arrowRGB
        }
        // 回收 recycle
        attributes.recycle()
        // endregion
    }

    //region 设置资源

    /**
     * 设置整个item的对象info
     */
    fun setInfo(info: ItemSettingsBean) {
        itemBean = info
        obItemInfo.set(info)
    }

    /**
     * 设置title
     */
    fun setTitle(title: String) {
        itemBean.title = title
        // obItemInfo.notifyChange()
    }

    /**
     * 设置内容描述
     */
    fun setDesc(desc: String) {
        itemBean.desc = desc
        // obItemInfo.notifyChange()
    }

    /**
     * 设置icon图标
     */
    fun setIcon(iconRes: Any) {
        itemBean.iconRes = iconRes
        // obItemInfo.notifyChange()
    }

    /**
     * 设置icon图标
     */
    fun setArrow(arrowRes: Any) {
        itemBean.arrowRes = arrowRes
        // obItemInfo.notifyChange()
    }
    //endregion

    //region 点击事件
    //单独点击icon
    fun onClickIcon(listener: OnClickListener) {
        itemBean.iconListener = listener
        // obItemInfo.notifyChange()
    }

    //单独点击title
    fun onClickTitle(listener: OnClickListener) {
        itemBean.titleListener = listener
        // obItemInfo.notifyChange()
    }

    //单独点击desc
    fun onClickDesc(listener: OnClickListener) {
        itemBean.descListener = listener
        // obItemInfo.notifyChange()
    }

    //单独点击箭头
    fun onClickArrow(listener: OnClickListener) {
        itemBean.arrowListener = listener
        // obItemInfo.notifyChange()
    }
    //endregion

    //region 设置颜色
    /**
     * 设置icon颜色
     */
    fun setIconColor(colorRes: Int) {
        itemBean.iconColor = colorRes
        // obItemInfo.notifyChange()
    }

    /**
     * 设置title标题颜色
     */
    fun setTitleColor(colorRes: Int) {
        itemBean.titleColor = colorRes
        // obItemInfo.notifyChange()
    }

    /**
     * 设置desc副标题颜色
     */
    fun setDescColor(colorRes: Int) {
        itemBean.descColor = colorRes
        // obItemInfo.notifyChange()
    }

    /**
     * 设置箭头arrow颜色
     */
    fun setArrowColor(colorRes: Int) {
        itemBean.arrowColor = colorRes
        // obItemInfo.notifyChange()
    }

    //endregion

    /*
    * 重写拦截事件，如果设置了整个item的点击，就让子view的点击不再响应，为了可以item全区域响应该点击事件
    * */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return hasOnClickListeners()
    }

}

//3、定义绑定的实体类
@Keep
data class ItemSettingsBean(
    var iconRes: Any = R.drawable.ic_gift_card, //左边图标 有可能是url string, uri
    var title: String = "", //title标题
    var desc: String = "", //副标题，描述
    var titleColor: Int = R.color.colorPrimaryText, //title字的颜色
    var descColor: Int = R.color.colorSecondaryText, //副标题字的颜色
    var iconColor: Int = 0, //icon的颜色
    var arrowColor: Int = 0, //右边箭头图片的颜色
    var arrowRes: Any = R.drawable.ic_right //右边箭头图片
) {
    //itemview的子view点击事件
    var iconListener: View.OnClickListener? = null
    var titleListener: View.OnClickListener? = null
    var descListener: View.OnClickListener? = null
    var arrowListener: View.OnClickListener? = null
}