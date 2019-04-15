package com.example.testtask.model

import com.google.gson.annotations.SerializedName

class PlaylistResponse(
    @SerializedName("items") val items: ArrayList<PlaylistItem>
)