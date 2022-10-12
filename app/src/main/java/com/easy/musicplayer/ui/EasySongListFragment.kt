package com.easy.musicplayer.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.easy.musicplayer.MainActivity
import com.easy.musicplayer.R

class EasySongListFragment : Fragment() {

    companion object {
        fun newInstance() = EasySongListFragment()
    }

    private val mViewModel: EasySongListViewModel by viewModels {
        EasySongListViewModel.Factory(requireContext().contentResolver)
    }

    private lateinit var mListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_easy_song_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        initObserver()

        openMediaStore()
    }

    private fun initView() {
        mListView = view?.findViewById(R.id.easy_song_list_list_view)!!
    }

    private fun initObserver() {
        mViewModel.songs.observe(viewLifecycleOwner) { songs ->
            val songsName = songs.map { it.artistName }
            mListView.adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, songsName)
        }
    }

    /**
     * 读取本地的音频文件
     * .. 有权限直接读取，没有的话需要申请权限
     */
    private fun openMediaStore() {
        if (haveStoragePermission()) {
            showSongs()
        } else {
            requestPermission()
        }
    }

    private fun showSongs() {
        mViewModel.loadSongs()
    }

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { succ ->
                if (succ) {
                    showSongs()
                } else {
                    Toast.makeText(requireContext(), "授权失败", Toast.LENGTH_SHORT).show()
                }
            }.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    /**
     * 是否给了读取文件的权限
     */
    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

}