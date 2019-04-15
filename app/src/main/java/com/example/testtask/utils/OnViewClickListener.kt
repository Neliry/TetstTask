package com.example.testtask.utils

import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import com.example.testtask.entities.VideoEntity
import com.example.testtask.model.Video

interface OnViewClickListener{
    fun onViewClicked(view: View?, playlistItem: VideoEntity, image: ImageView, connMgr: ConnectivityManager)
}