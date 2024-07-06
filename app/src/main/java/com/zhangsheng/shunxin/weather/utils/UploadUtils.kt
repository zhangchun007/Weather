package com.zhangsheng.shunxin.weather.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.autonavi.base.amap.mapcore.tools.GLFileUtil
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.xToast
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.AppUtils
import com.zhangsheng.shunxin.weather.net.bean.ControlBean
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/23 22:39
 */
object UploadUtils {
    private var root_dir = ""
    private var file_dir = ""
    var progress: Int = 0

    fun startUpdate(control: ControlBean?, func: (cancel: Boolean, progress: Int) -> Unit) {
        var fileName =
            "weather_${control.nN().android_software_update.nN().update2v}.apk"
        root_dir = GLFileUtil.getCacheDir(AppContext.getContext()).absolutePath + File.separator + "${AppUtils.appName}"
        file_dir = root_dir + File.separator + fileName
        var isFileExits = false
        val folder = File(root_dir)
        if (!folder.exists())
            folder.mkdir()
        if (folder.isDirectory) { // 删除历史版本
            val files = folder.list()
            if (files.contains(fileName))
                isFileExits = true
            files.forEach {
                var prefix = ""
                Try { prefix = it.substring(it.lastIndexOf(".") + 1) }
                val innerFile = File(root_dir + File.separator + it)
                if (innerFile.exists() && innerFile.isFile && prefix == "apk") {
                    if (it != fileName) {
                        innerFile.delete()
                    } else if (getFileMD5(innerFile) != control.nN().android_software_update.nN().file_md5) {
                        innerFile.delete()
                        isFileExits = false
                    }
                }
            }
        }
        if (isFileExits) {
            startInstall()
        } else {
            startDownload(control, func)
        }
    }

    private fun startDownload(
        control: ControlBean?,
        func: (cancel: Boolean, progress: Int) -> Unit
    ) {
        var downloadTask = FileDownloader.getImpl()
            .create(control.nN().android_software_update.nN().url)
            .setPath(file_dir)
            .setListener(object : FileDownloadListener() {
                override fun warn(p0: BaseDownloadTask?) {
                }

                override fun completed(p0: BaseDownloadTask?) {
                    startInstall()
                    func(false, 100)
                }

                override fun pending(p0: BaseDownloadTask?, p1: Int, p2: Int) {
                }

                override fun error(p0: BaseDownloadTask?, p1: Throwable?) {
                    xToast("下载超时，请重试")
                    func(true, 0)
                }

                override fun progress(p0: BaseDownloadTask?, p1: Int, p2: Int) {
                    if (((p1.toFloat() / p2.toFloat()) * 100).toInt() > progress) {
                        progress = ((p1.toFloat() / p2.toFloat()) * 100).toInt()
                        func(false, progress)
                    }
                }

                override fun paused(p0: BaseDownloadTask?, p1: Int, p2: Int) {
                }
            })
        downloadTask!!.start()
    }

    fun startInstall() {
        AppContext.getContext().startActivity(Intent(Intent.ACTION_VIEW).apply {
            try {
                Runtime.getRuntime().exec("chmod 777 $root_dir").waitFor()
                Runtime.getRuntime().exec("chmod 777 $file_dir").waitFor()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                setDataAndType(
                    FileProvider.getUriForFile(
                        AppContext.getContext(),
                        "${AppContext.getContext().packageName}.fileprovider",
                        File(file_dir)
                    ), "application/vnd.android.package-archive"
                )
            } else {
                setDataAndType(
                    Uri.fromFile(File(file_dir)),
                    "application/vnd.android.package-archive"
                )
            }
        })
    }

    @Throws(NoSuchAlgorithmException::class, IOException::class)
    private fun getFileMD5(file: File): String? {
        if (!file.isFile) {
            return null
        }
        val buffer = ByteArray(1024)
        var len: Int
        val digest: MessageDigest = MessageDigest.getInstance("MD5")
        val `in`: FileInputStream = FileInputStream(file)
        while (`in`.read(buffer, 0, 1024).also { len = it } != -1) {
            digest.update(buffer, 0, len)
        }
        `in`.close()
        val bigInt = BigInteger(1, digest.digest())
        return bigInt.toString(16)
    }
}