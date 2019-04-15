package com.example.testtask.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos_table")
class VideoEntity(
    @field:ColumnInfo(name = "video_title")
    val videoTitle: String,

    @field:ColumnInfo(name = "video_description")
    var videoDescription: String,

    @field:ColumnInfo(name = "video_thumbnail")
    var videoThumbnail: String,

    @field:ColumnInfo(name = "video_url")
    var videoId: String,

    @field:ColumnInfo(name = "video_playlist_id")
    var videoPlaylistId: String)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}