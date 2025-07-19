package com.samuelrmos.fusechallenge.data.state

import com.samuelrmos.fusechallenge.data.MatchItem

data class MatchesListState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val response:  List<MatchItem?>? = null
)