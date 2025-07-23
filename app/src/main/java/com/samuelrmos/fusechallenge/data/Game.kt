package com.samuelrmos.fusechallenge.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    @SerializedName("begin_at")
    val beginAt: String?,
    val status: String
): Parcelable