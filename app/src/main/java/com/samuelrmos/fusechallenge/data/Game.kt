package com.samuelrmos.fusechallenge.data

import com.google.gson.annotations.SerializedName

data class Game(
    @SerializedName("begin_at")
    val beginAt: String,
    val status: String
)