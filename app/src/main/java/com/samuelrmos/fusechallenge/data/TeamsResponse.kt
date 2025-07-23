package com.samuelrmos.fusechallenge.data

import com.google.gson.annotations.SerializedName

data class TeamsResponse(
    @SerializedName("name")
    val teamName: String,
    val players: List<Players?>? = null
)