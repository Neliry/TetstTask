package com.example.testtask.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.example.testtask.entities.VideoEntity

@Dao
interface VideoDao {

    @get:Query("SELECT*FROM videos_table ORDER BY id DESC")
    val getAllVideos: LiveData<List<VideoEntity>>

    @Query("SELECT * FROM videos_table WHERE video_playlist_id = :playlistId")
    fun loadVideosByPlaylist(playlistId: String): List<VideoEntity>

    @Query("SELECT * FROM videos_table WHERE video_url = :videoId")
    fun loadVideosById(videoId: String): List<VideoEntity>

    @Insert
    fun insert(video: VideoEntity)

}