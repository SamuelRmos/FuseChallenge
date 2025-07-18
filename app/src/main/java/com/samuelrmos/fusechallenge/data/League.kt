package com.samuelrmos.fusechallenge.data

import com.google.gson.annotations.SerializedName

data class League(
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    val name: String
)
