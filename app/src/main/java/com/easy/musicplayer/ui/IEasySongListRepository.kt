package com.easy.musicplayer.ui

import com.easy.musicplayer.data.Song

interface IEasySongListRepository {
    suspend fun getSongs(): List<Song>
}