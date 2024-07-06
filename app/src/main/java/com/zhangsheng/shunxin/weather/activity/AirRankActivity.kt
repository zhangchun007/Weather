package com.zhangsheng.shunxin.weather.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import com.maiya.thirdlibrary.adapter.CommonAdapter
import com.maiya.thirdlibrary.utils.PicUtils
import com.maiya.thirdlibrary.adapter.ViewHolder
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityAirRankBinding
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.model.AirModel
import com.zhangsheng.shunxin.weather.net.bean.AirRankBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import org.koin.android.ext.android.inject

class AirRankActivity : AacActivity<AirModel>() {
    private var datas: ArrayList<AirRankBean.RankingsBean> = ArrayList()
    private var first = true
    private var sequence = true
    private var adapter: AirRankActivity.Adapter? = null
    private var ranking = 0
    private var regioncode = ""
    override val vm by inject<AirModel>()
    override val binding by inflate<ActivityAirRankBinding>()

    override fun initView(savedInstanceState: Bundle?) {
        Try {
            regioncode = intent.getStringExtra("code").isStr("")
        }

        binding.title.initTitle("全国空气质量排名")
        binding.lv.adapter = getAdapter()

        binding.sort.setSingleClickListener {
            if (datas.isNotEmpty()) {
                if (sequence) {
                    datas.sortByDescending { it.ranking }
                } else {
                    datas.sortBy { it.ranking }
                }
                sequence = !sequence
                binding.imageSort.toRotation(180f)
                getAdapter().notifyDataSetChanged()

                if (sequence) {
                    if (ranking > 10) {
                        binding.lv.post { binding.lv.setSelection(ranking - 3) }
                    }
                } else {
                    if ((datas.size - ranking) > 10) {
                        binding.lv.post { binding.lv.setSelection(datas.size - ranking - 2) }
                    }
                }
            }
        }
    }

    private fun rankUi() {
        if (datas.nN().isNotEmpty()) {
            if (first) {
                PicUtils.tintColor(
                    binding.left,
                    WeatherUtils.airColorStr(datas.listIndex(0).nN().aqiLevel)
                )
                PicUtils.tintColor(
                    binding.right,
                    WeatherUtils.airColorStr(datas.listIndex(datas.nN().size - 1).nN().aqiLevel)
                )
                first = false
                binding.leftName.text = datas.listIndex(0).city
                binding.rightName.text = datas.listIndex(datas.nN().size - 1).nN().city
                binding.leftAqi.text = "AQI ${datas.listIndex(0).nN().aqi}"
                binding.rightAqi.text = "AQI ${datas.listIndex(datas.nN().size - 1).nN().aqi}"
            }
            getAdapter().notifyDataSetChanged()
        }

        var item = datas.nN()
            .filter { it.areaCodes.contains(regioncode) }
        if (item.isNotEmpty()) {
            if (item.listIndex(0).ranking > 10) {
                binding.lv.post { binding.lv.setSelection(item.listIndex(0).ranking - 3) }
                ranking = item.listIndex(0).ranking
            }

        }
    }


    private fun getAdapter(): AirRankActivity.Adapter {
        if (adapter == null) {
            adapter = Adapter(this)
        }
        return adapter!!
    }

    override fun initObserve() {
        super.initObserve()
        if (getAppModel().airRank.value.nN().rankings.isNullOrEmpty()) {
            vm.rankRequst()
        }

        getAppModel().airRank.safeObserve(this, Observer {
            datas.clear()
            datas.addAll(it.nN().rankings.nN())

            binding.title.setRightTvStr(
                "${
                    DataUtil.date2date(
                        it.nN().time,
                        "yyyy-MM-dd HH:mm:ss",
                        "HH:mm"
                    )
                }发布"
            )
            binding.title.setRightTvColor("#999999")
            rankUi()
        })
    }

    override fun retry() {
        super.retry()
        vm.rankRequst()
    }

    inner class Adapter(context: Context) :
        CommonAdapter<AirRankBean.RankingsBean>(context, datas.nN(), R.layout.item_air_rank) {
        override fun convert(holder: ViewHolder, t: AirRankBean.RankingsBean?, position: Int) {
            holder.setText(R.id.rank, "${t.nN().ranking}")
            holder.setText(R.id.city, t.nN().city)
            holder.setText(R.id.district, t.nN().province)
            var shap = holder.getView<ShapeView>(R.id.shape_air)
            var bg = holder.getView<RelativeLayout>(R.id.rl)
            shap.text = t.nN().aqi
            WeatherUtils.airColor(t.nN().aqiLevel, shap)
            if (position % 2 != 0) {
                bg.setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                bg.setBackgroundColor(Color.parseColor("#FFF4F5F9"))
            }

            bg.setSingleClickListener {
                Try {
                    skipActivity(AirActivity::class.java) {
                        putExtra("code", t.nN().areaCodes.split(",").listIndex(0))
                        putExtra("name", t.nN().city)
                        putExtra("location", false)
                    }
                }
            }
        }

    }

}