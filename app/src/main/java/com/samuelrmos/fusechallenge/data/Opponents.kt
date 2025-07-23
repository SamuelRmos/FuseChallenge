package com.samuelrmos.fusechallenge.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Opponents(
    @SerializedName("opponent")
    val opponentDescriptions: OpponentDescriptions,
    val type: String
) : Parcelable
