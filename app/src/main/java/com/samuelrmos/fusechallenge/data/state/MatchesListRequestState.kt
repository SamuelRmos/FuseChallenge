package com.samuelrmos.fusechallenge.data.state

import com.samuelrmos.fusechallenge.data.MatchesResponse

const val errorMessage = "Something went wrong"

sealed class MatchesListRequestState {
    data object Loading : MatchesListRequestState()
    data class Success(val response: MutableList<MatchesResponse?>) : MatchesListRequestState()
    data class Error(val message: String = errorMessage) : MatchesListRequestState()
}