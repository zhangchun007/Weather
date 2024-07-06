package com.zhangsheng.shunxin.weather.fragment

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.utils.StatusBarUtil
import com.maiya.weather.information.bean.InfoFragmentSkip
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.InformationStreamBinding
import com.zhangsheng.shunxin.information.adapter.InfoVpAdapter
import com.zhangsheng.shunxin.information.constant.Constants
import com.zhangsheng.shunxin.information.dialog.ChannelDialog
import com.zhangsheng.shunxin.information.widget.tablayout.TabLayout
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus.getChannel
import com.zhangsheng.shunxin.weather.utils.NewsChannelDataUtils
import org.koin.android.ext.android.inject

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/8/28 15:50
 */
class NewsFragment : AacFragment<BaseViewModel,InformationStreamBinding>(R.layout.information_stream) {
    override val vm: BaseViewModel by inject()

    private var curPos: Int = 0
    private var chanDialog: ChannelDialog? = null


    override fun initView() {
        binding.topStutas.setPadding(0, StatusBarUtil.getStatusBarHeight(activity), 0, 0)
        Constants.curTabCode = "__all__ "
        initSmartTabLayout(0)
    }
    fun initTabLayoutData(){
//        Log.w("lpb","NewsFragment---initTabLayoutData")
        initSmartTabLayout(0)
    }

    override fun initObserve() {
        getChannel<InfoFragmentSkip>("InfoFragmentSkip").safeObserve(this, Observer {
//                Log.w("lpb", "InfoFragmentSkip.getChanne------>receive")
            initSmartTabLayout(it.nN().pos)
        })
    }

    override fun initListener() {
        super.initListener()
        //点击信息流频道
        binding.ivChannel.setSingleClickListener {

            chanDialog = activity?.let { ChannelDialog(it) }

            if (chanDialog != null) {
                chanDialog!!.setData(curPos)
                var isFinish = activity?.isFinishing
                if (isFinish != null && !isFinish) {
                    chanDialog!!.show()
                }
            }

        }
    }

    private fun initSmartTabLayout(pos: Int) {
        curPos = pos
        var informAdapter = InfoVpAdapter(childFragmentManager)
        binding.weatherNewsViewpager.adapter = informAdapter

        //处理数据
        if (informAdapter.count > 0) {
            binding.weatherNewsViewpager.currentItem = 0
            return
        }
        var tabList= context?.let { NewsChannelDataUtils.getNewTabList(it) }
        if (tabList==null||tabList?.size == 0) {
            return
        }


        informAdapter.replace(tabList)
//        weather_news_viewpager.offscreenPageLimit = 0
        // 要设置到viewpager.setAdapter后才起作用
        if (pos == -1) {
            binding.weatherNewsViewpager.setCurrentItem(tabList.size - 1)
        } else {
            binding.weatherNewsViewpager.setCurrentItem(pos)
        }
        binding.weatherNewsViewpager.addOnAttachStateChangeListener(onAttachStateChangeListener)
        binding.weatherNewsViewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
//                Log.w("lpb", "----->setOnPageChangeListener:" + i)
                curPos = i

                if (tabList != null && tabList.size > i) {
                    Constants.curTabCode = tabList.nN().get(i).code
                    Constants.curTabTitle = tabList.nN().get(i).title
//                    CommUtil.infoTabCLickOrDetailsShowReport("1", i)
                }
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })
        binding.weatherNewsIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabView: TabLayout.TabView = tab.mView
                if (tabView.mTextView != null) {
                    tabView.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18F)
                    tabView.mTextView.setTextColor(Color.parseColor("#379BFF"))
                    tabView.mTextView.getPaint().setFakeBoldText(true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabView: TabLayout.TabView = tab.mView
                if (tabView.mTextView != null) {
                    tabView.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16F)
                    tabView.mTextView.setTextColor(Color.parseColor("#999999"))
                    tabView.mTextView.getPaint().setFakeBoldText(false)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.weatherNewsIndicator.setupWithViewPager(binding.weatherNewsViewpager)

    }

    private var onAttachStateChangeListener: View.OnAttachStateChangeListener =
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                if (view is ViewPager) {
                    //以反射的形式处理 recyclerview + viewepager时 viewpager 被销毁重新进入window时 mFirstLayout 为true 导致的
                    //viewpager第一次滑动失效的问题
                    try {
                        val superclass: Class<*> = view.javaClass
                        val field =
                            superclass.getDeclaredField("mFirstLayout")
                        field.isAccessible = true
                        field.setBoolean(view, false)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onViewDetachedFromWindow(view: View) {}
        }

    override fun onResume() {
        super.onResume()
        if (isVisible) {
            XMLogAgent.onPageStart(EnumType.上报埋点.信息流主页)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isVisible) {
            XMLogAgent.onPageEnd(EnumType.上报埋点.信息流主页)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            XMLogAgent.onPageEnd(EnumType.上报埋点.信息流主页)
        } else {
            XMLogAgent.onPageStart(EnumType.上报埋点.信息流主页)
        }
    }

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): InformationStreamBinding =InformationStreamBinding.inflate(inflater,viewGroup,false)

    override fun onDestroy() {
        super.onDestroy()
        clearBinding()
    }

}