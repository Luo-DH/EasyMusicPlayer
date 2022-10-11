package com.easy.lib.musicservice

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log

class EasyMusicServiceHelper private constructor(private val mApplicationContext: Context) {

    companion object {

        const val TAG = "EasyMusicServiceHelper"

        @Volatile
        private var INSTANCES: EasyMusicServiceHelper? = null
        fun getInstances(applicationContext: Context): EasyMusicServiceHelper {
            if (INSTANCES == null) {
                synchronized(EasyMusicServiceHelper::class) {
                    if (INSTANCES == null) {
                        INSTANCES = EasyMusicServiceHelper(applicationContext)
                    }
                }
            }
            return INSTANCES!!
        }
    }

    private val mMediaBrowserCompat = MediaBrowserCompat(
        mApplicationContext, ComponentName(mApplicationContext, EasyMusicService::class.java),
        ConnectionCallback(), null
    ).apply { connect() }

    lateinit var mMediaController: MediaControllerCompat

    // 用于控制歌曲的播放/暂停
    val transportControls: MediaControllerCompat.TransportControls
        get() = mMediaController.transportControls

    inner class ConnectionCallback : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            Log.d(TAG, "onConnected: ")
            super.onConnected()

            mMediaController =
                MediaControllerCompat(mApplicationContext, mMediaBrowserCompat.sessionToken)

            mMediaController.registerCallback(object : MediaControllerCompat.Callback() {
                override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                    super.onPlaybackStateChanged(state)
                    Log.d(TAG, "onPlaybackStateChanged: 歌曲状态回调: $state")
                }

                override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                    super.onMetadataChanged(metadata)
                    Log.d(TAG, "onMetadataChanged: 播放媒体文件改变 $metadata")
                }

                override fun onSessionEvent(event: String?, extras: Bundle?) {
                    super.onSessionEvent(event, extras)
                    Log.d(TAG, "onSessionEvent: $event")
                }

            })
        }

        override fun onConnectionFailed() {
            Log.d(TAG, "onConnectionFailed: ")
            super.onConnectionFailed()
        }
    }

}

