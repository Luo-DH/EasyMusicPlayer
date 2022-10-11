package com.easy.lib.musicservice

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

internal class EasyMusicService : MediaBrowserServiceCompat() {

    companion object {
        const val TAG = "EasyMusicService"
    }

    lateinit var mMediaSession: MediaSessionCompat
    lateinit var mPlayer: ExoPlayer

    private val dataSourceFactory: DefaultDataSource.Factory by lazy {
//        DefaultDataSourceFactory(this, Util.getUserAgent(this, MUSIC_USER_AGENT), null)
        DefaultDataSource.Factory(applicationContext)
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        super.onCreate()

        mMediaSession = MediaSessionCompat(this, "MediaSessionTag").also {
            it.isActive = true
        }

        // 初始化播放器
        mPlayer = ExoPlayer.Builder(applicationContext).build().apply {
            setAudioAttributes(audioAttributes, true)
            setHandleAudioBecomingNoisy(true)
        }

        MediaSessionConnector(mMediaSession).also {
            it.setPlaybackPreparer(EasyMusicPlaybackPreparer(
                mPlayer, dataSourceFactory
            ))
        }

        // 没有这一行的话。。。connect()方法就不知道干嘛去了，一直等不到回掉的
        sessionToken = mMediaSession.sessionToken
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")

        mMediaSession.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        Log.d(
            TAG,
            "onGetRoot: clientPackageName=$clientPackageName clientUid=$clientUid rootHints=$rootHints"
        )
        return BrowserRoot("", null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        Log.d(TAG, "onLoadChildren: parentId=$parentId")
    }
}