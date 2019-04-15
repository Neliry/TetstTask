package com.example.testtask.network

import com.example.testtask.model.PlaylistResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface  YoutubeService {
    @GET("playlistItems")
    fun getPlayListItems(
        @Query("playlistId") playlistId: String,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: String = "30",
        @Query("key") key: String = "AIzaSyCUU8jLzrxH5aYcCS9JI01NaxCD-yjLvt0"
    ): Call<PlaylistResponse>
}