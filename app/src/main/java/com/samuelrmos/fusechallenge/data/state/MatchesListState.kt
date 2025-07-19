package com.samuelrmos.fusechallenge.data.state

import com.samuelrmos.fusechallenge.data.MatchesResponse

data class MatchesListState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val response:  List<MatchesResponse?>? = null
)