package com.example.testtask.model

import com.google.gson.annotations.SerializedName

class Snippet (
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnails") val thumbnail: Thumbnail,
    @SerializedName("resourceId") val resourceId: ResourceId
    )