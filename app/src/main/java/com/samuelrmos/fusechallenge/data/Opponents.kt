package com.samuelrmos.fusechallenge.data

import com.google.gson.annotations.SerializedName

data class Opponents(
    @SerializedName("opponent")
    val opponentDescriptions: OpponentDescriptions,
    val type: String
)
