package com.zhangsheng.shunxin.weather.utils.alispeak

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.xToast
import kotlinx.coroutines.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.collections.HashMap

object SpeakerUtils {
    private var isFlu = false
    private var regionCode = "-1"
    private var fluPlaying = false
    private var isTimer = false
    private const val SAMPLE_RATE = 16000
    private var voice = HashMap<String, ByteArray?>()
    private var audioTrack: AudioTrack? = null
    private var prepare = ByteArray(0)
    private var tempData: ByteArray? = ByteArray(0)
    private val audioQueue: LinkedBlockingQueue<ByteArray?> = LinkedBlockingQueue<ByteArray?>()
    private var speakListener: (isPlay: Boolean) -> Unit = {}
    private val iMinBufSize = AudioTrack.getMinBufferSize(
        SAMPLE_RATE,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )


    fun timerCancel() {
        isTimer = false
    }

    fun requestPrepareListener(timeOut: Long, func: () -> Unit) {
        isTimer = true

        GlobalScope.async {
            delay(timeOut)
            withContext(Dispatchers.Main) {
                if (!isFlu && !fluPlaying && isTimer) {
                    func()
                }
            }
        }

    }


    private fun playMp3(context: Context, byteArray: ByteArray, code: String) {

        GlobalScope.async {
            try {
                if (audioTrack == null) {
                    audioTrack = AudioTrack(
                        AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_MONO
                        , AudioFormat.ENCODING_PCM_16BIT,
                        iMinBufSize * 10, AudioTrack.MODE_STREAM
                    )
                }
                audioTrack?.play()
                audioTrack?.write(byteArray, 0, byteArray.size)
                withContext(Dispatchers.Main) {
                    delay(800)
                    stop()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    clearVoice(code)
                    stop()
                    xToast("播放失败")
                }
            }
        }
    }

    private fun playFlu() {
        fluPlaying = true
        speakListener(true)
        isFlu = true
        if (audioTrack == null) {
            audioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO
                , AudioFormat.ENCODING_PCM_16BIT,
                iMinBufSize * 10, AudioTrack.MODE_STREAM
            )
        }
        GlobalScope.async {
            try {
                while (fluPlaying) {
                    tempData = audioQueue.poll()
                    if (tempData == null) {
                        delay(20)
                        if (!isFlu) {
                            delay(800)
                            withContext(Dispatchers.Main) {
                                stop()
                            }
                        }
                    } else {
                        if (audioTrack.nN().playState != AudioTrack.PLAYSTATE_PLAYING) {
                            audioTrack.nN().play()
                        }
                        audioTrack.nN().write(tempData.nN(), 0, tempData.nN().size)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    xToast("播放错误，请重试")
                    stop()
                }
            }
        }

    }


    fun autoPlay(context: Context, code: String) {
        if (isPlaying()) {
            stop()
        } else {
            speakListener(true)
            voice[code]?.let { playMp3(context, it, code) }
        }
    }

    fun stop() {
        Try {
            speakListener(false)
            fluPlaying = false
            isFlu =false
            regionCode = "-1"
            audioQueue.clear()
            tempData = ByteArray(0)
            prepare = ByteArray(0)
            audioTrack?.pause()
            audioTrack?.flush()
        }
    }

    fun isPlaying(): Boolean {
        return if (audioTrack == null) false else audioTrack!!.playState == AudioTrack.PLAYSTATE_PLAYING
    }

    fun addVoice(code: String) {
        if (code == regionCode) {
            if (prepare.isNotEmpty()) {
                voice[code] = prepare
                isFlu = false
                prepare = ByteArray(0)
            }
        } else {
            clearVoice(regionCode)
            stop()
        }

    }

    fun checkSpeaker(code: String): Boolean {
        return voice[code] != null
    }

    fun clearVoice(code: String) {
        prepare = ByteArray(0)
        voice[code]?.let {
            voice[code] = null
        }
    }


    fun prepare(byteArray: ByteArray?, code: String) {
        if (regionCode == "-1") {
            regionCode = code
        } else if (regionCode == code) {
            if (byteArray != null) {
                audioQueue.offer(byteArray)
                prepare = prepare.plus(byteArray)
                if (!isFlu) {
                    playFlu()
                }
            }
        }

    }

    fun setOnSpeakListener(func: (isPlay: Boolean) -> Unit) {
        speakListener = func
    }

}