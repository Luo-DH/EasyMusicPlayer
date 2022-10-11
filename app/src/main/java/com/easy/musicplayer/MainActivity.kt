package com.easy.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.easy.lib.musicservice.EasyMusicServiceHelper

class MainActivity : AppCompatActivity() {

    private lateinit var mEasyMusicServiceHelper: EasyMusicServiceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEasyMusicServiceHelper = EasyMusicServiceHelper.getInstances(applicationContext)

        findViewById<TextView>(R.id.test_play).setOnClickListener {
            mEasyMusicServiceHelper.transportControls.playFromMediaId("testId", Bundle())
            // test
        }
    }
}