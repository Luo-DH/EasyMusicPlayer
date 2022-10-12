package com.easy.musicplayer.ui

import android.content.ContentResolver
import android.database.ContentObserver
import android.provider.MediaStore
import androidx.lifecycle.*
import com.easy.musicplayer.EasyMusicApplication
import com.easy.musicplayer.data.Song
import com.easy.musicplayer.registerObserver
import kotlinx.coroutines.launch

class EasySongListViewModel(private val contentResolver: ContentResolver) : ViewModel() {

    private val mEasySongListRepository: IEasySongListRepository
            by lazy { EasySongListRepositoryImpl(contentResolver) }

    private var contentObserver: ContentObserver? = null
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs

    fun loadSongs() {
        viewModelScope.launch {
            val songs = mEasySongListRepository.getSongs()
            _songs.postValue(songs)
            if (contentObserver == null) {
                contentObserver = contentResolver.registerObserver(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                ) {
                    loadSongs()
                }
            }
        }
    }

    class Factory(
        private val contentResolver: ContentResolver,
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EasySongListViewModel(
                contentResolver,
            ) as T
        }
    }
}