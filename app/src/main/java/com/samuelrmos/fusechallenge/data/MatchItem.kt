package com.samuelrmos.fusechallenge.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchItem(
    val beginAt: String?,
    val game: Game,
    val serie: Serie,
    val league: League,
    val firstOpponent: Opponents,
    val secondOpponent: Opponents
) : Parcelable