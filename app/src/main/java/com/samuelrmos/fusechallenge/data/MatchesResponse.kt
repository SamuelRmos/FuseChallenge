package com.samuelrmos.fusechallenge.data

import com.google.gson.annotations.SerializedName

data class MatchesResponse(
    @SerializedName("begin_at")
    val beginAt: String?,
    val games: List<Game>,
    val serie: Serie,
    val league: League,
    val opponents: List<Opponents>
)
