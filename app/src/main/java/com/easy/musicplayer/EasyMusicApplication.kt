package com.easy.musicplayer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class EasyMusicApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}