package com.example.testtask.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import android.os.AsyncTask
import com.example.testtask.daos.VideoDao
import com.example.testtask.databases.VideosDatabase
import com.example.testtask.entities.VideoEntity
import com.example.testtask.model.PlaylistResponse
import com.example.testtask.network.YoutubeService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.LiveData
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.google.android.youtube.player.internal.e
import java.nio.file.Files.delete
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import android.R.attr.bitmap
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import java.io.IOException


class Repository(application: Application) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/youtube/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val youtubeService = retrofit.create(YoutubeService::class.java)
    private val videoDao: VideoDao
    private val playList : MutableLiveData<List<VideoEntity>> by lazy {
        MutableLiveData<List<VideoEntity>>()}

    init {
        val database = VideosDatabase.getInstance(application)
        videoDao = database.videoDao()
        playList.value = videoDao.getAllVideos.value
    }

    fun getPlayList(playlistId: String): Call<PlaylistResponse> {
        return youtubeService.getPlayListItems(playlistId)
    }

    fun loadVideosByPlaylist(playlistId: String){
        LoadVideosByPlaylistAsyncTask(videoDao, playList).execute(playlistId)
    }

    fun saveClickedVideo(video: VideoEntity, bitmap: Bitmap, context: Context){
        LoadVideosByIdAsyncTask(videoDao, bitmap, context).execute(video)
    }

    fun getSavedVideo():  MutableLiveData<List<VideoEntity>>{
        return playList
    }
    private class LoadVideosByPlaylistAsyncTask internal constructor(private val videoDao: VideoDao, val playList: MutableLiveData<List<VideoEntity>>) : AsyncTask<String, Void, List<VideoEntity>>() {
        override fun doInBackground(vararg plylistId: String): List<VideoEntity>? {
            return videoDao.loadVideosByPlaylist(plylistId[0])
        }

        override fun onPostExecute(result: List<VideoEntity>?) {
            super.onPostExecute(result)
            playList.value = result
        }
    }

    private class LoadVideosByIdAsyncTask internal constructor(private val videoDao: VideoDao, val bitmap: Bitmap, val context: Context) : AsyncTask<VideoEntity, Void, Void>() {
        override fun doInBackground(vararg video: VideoEntity): Void? {
            if(videoDao.loadVideosById(video[0].videoId).isEmpty()){
                video[0].videoThumbnail = saveImage(bitmap, System.currentTimeMillis().toString() + ".jpg", context)
                videoDao.insert(video[0])
            }
            return null
        }

        fun saveImage(bitmap: Bitmap, filename: String, context: Context): String {

            val wrapper = ContextWrapper(context)

            var file = wrapper.getDir("Images", MODE_PRIVATE)

            file = File(file, filename)

            try {
                var stream: OutputStream? = null
                stream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
            } catch (e: IOException) // Catch the exception
            {
                e.printStackTrace()
            }

            val savedImageURI = Uri.parse(file.absolutePath)
            return savedImageURI.toString()
        }
    }
}