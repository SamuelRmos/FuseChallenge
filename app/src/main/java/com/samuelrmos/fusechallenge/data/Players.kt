package com.samuelrmos.fusechallenge.data

import com.google.gson.annotations.SerializedName

data class Players(
    @SerializedName("name")
    val nickName: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("image_url")
    val imageUrl: String?
)