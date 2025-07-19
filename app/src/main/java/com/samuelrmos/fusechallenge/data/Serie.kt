package com.samuelrmos.fusechallenge.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Serie(
    val id: Int,
    val name: String?
): Parcelable
