package com.easy.lib.musicservice

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class EasyMusicPlaybackPreparer(
    private val mPlayer: ExoPlayer,
    private val mDataSourceFactory: DefaultDataSource.Factory
) : MediaSessionConnector.PlaybackPreparer {


    override fun onCommand(
        player: Player,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ): Boolean {
        return false
    }

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    override fun onPrepare(playWhenReady: Boolean) {
    }

    override fun onPrepareFromMediaId(
        mediaId: String,
        playWhenReady: Boolean,
        extras: Bundle?
    ) {
        val songUrl1 = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"
        val songUrl = Uri.parse(songUrl1)
        val a = ProgressiveMediaSource.Factory(mDataSourceFactory).createMediaSource(songUrl)
        mPlayer.setMediaSource(
            ConcatenatingMediaSource().apply {
                this.addMediaSource(a)
            }
        )
        mPlayer.prepare()
        mPlayer.play()
    }

    override fun onPrepareFromSearch(
        query: String,
        playWhenReady: Boolean,
        extras: Bundle?
    ) {
    }

    override fun onPrepareFromUri(
        uri: Uri,
        playWhenReady: Boolean,
        extras: Bundle?
    ) {
    }

}