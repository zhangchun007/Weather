package com.zhangsheng.shunxin.weather.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.maiya.thirdlibrary.ext.isStr
import com.maiya.thirdlibrary.ext.xToast
import com.maiya.thirdlibrary.utils.AppUtils
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.common.Configure
import com.zhangsheng.shunxin.weather.net.RetrofitFactory
import com.meituan.android.walle.WalleChannelReader
import com.zhangsheng.shunxin.ad.AdConstant
import kotlinx.android.synthetic.main.activity_bebug.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BebugActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bebug)

        tv1.text = "工作环境：" + if (Configure.Debug) "debug" else "release"

        tv2.text = "BASE_URL:" + RetrofitFactory.BASE_URL

        tv3.text = "包名:" + AppUtils.appPackageName

        tv4.text = "渠道号：" + WalleChannelReader.getChannel(this)

        tv5.text = "版本名称:" + AppUtils.appVersionName

        btn_ad.setOnClickListener {
            initAdIdBean(resources.getText(R.string.app_name).toString())
        }

    }

    private fun initAdIdBean(appName: String) {
        var jsonText: String? = Gson().toJson(AdConstant.getDefaultConfig()) ?: return
        saveToSDCard("testInfo", jsonText.isStr(""))
    }

    /**
     * 将json文件保存到sd目录下
     */
    var out: OutputStream? = null
    fun saveToSDCard(
        filename: String?,
        content: String
    ) {
        try {
            var dir: File = File(getExternalFilesDir(null).toString())
            val file = File(dir, filename)
            if (file.exists()) file.delete()
            out = FileOutputStream(file)
            out?.write(content.toByteArray())
            xToast("成功生成")

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (out != null) {
                    out?.close()
                    out = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

}