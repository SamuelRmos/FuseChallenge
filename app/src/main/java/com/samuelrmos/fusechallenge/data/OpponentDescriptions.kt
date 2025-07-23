package com.samuelrmos.fusechallenge.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpponentDescriptions(
    @SerializedName("image_url")
    val imageUrl: String?,
    val name: String?
) : Parcelable