package com.maiya.adlibrary.ad.widget;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.maiya.adlibrary.ijkplayer.IjkVideoView;
import com.xinmeng.shadow.base.IXMVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class ClientXMVideoView implements IXMVideoView,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnInfoListener {

    private IjkVideoView mVideoView;

    private OnPreparedListener mOuterPrepareCallback;
    private OnCompletionListener mOuterCompleteCallback;
    private OnErrorListener mOuterErrorCallback;
    private OnInfoListener mOuterInfoCallback;

    public ClientXMVideoView(Context context) {
        mVideoView = new IjkVideoView(context);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnInfoListener(this);
    }

    @Override
    public View asView() {
        return mVideoView;
    }

    @Override
    public void setOnPreparedListener(OnPreparedListener listener) {
        mOuterPrepareCallback = listener;
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        mOuterCompleteCallback = listener;
    }

    @Override
    public void setOnErrorListener(OnErrorListener listener) {
        mOuterErrorCallback = listener;
    }

    @Override
    public void setOnInfoListener(OnInfoListener listener) {
        mOuterInfoCallback = listener;
    }

    @Override
    public void setVideoURI(Uri uri) {
        mVideoView.setVideoURI(uri);
    }

    @Override
    public void start() {
        mVideoView.start();
    }

    @Override
    public void pause() {
        mVideoView.pause();
    }

    @Override
    public void stopPlayback() {
        mVideoView.stopPlayback();
    }

    @Override
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    @Override
    public int getCurrentStatus() {
        return mVideoView.getCurrentStatue();
    }

    @Override
    public void setKeepScreenOn(boolean keepScreenOn) {
        mVideoView.setKeepScreenOn(keepScreenOn);
    }

    @Override
    public int getDuration() {
        return mVideoView.getDuration();
    }

    @Override
    public void seekTo(int msec) {
        mVideoView.seekTo(msec);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mVideoView.setVolume(leftVolume, rightVolume);
    }

    @Override
    public int getCurrentPosition() {
        return mVideoView.getCurrentPosition();
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (null != mOuterPrepareCallback) {
            mOuterPrepareCallback.onPrepared();
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (null != mOuterCompleteCallback) {
            mOuterCompleteCallback.onCompletion();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        if (null != mOuterErrorCallback) {
            return mOuterErrorCallback.onError(i, i1);
        }
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        if (null != mOuterInfoCallback) {
            mOuterInfoCallback.onInfo(i, i1);
        }
        return false;
    }
}
