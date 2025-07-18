package com.samuelrmos.fusechallenge.data.state

import com.samuelrmos.fusechallenge.data.MatchesResponse

const val errorMessage = "Something went wrong"

sealed class ListMatchesRequestState {
    data object Loading : ListMatchesRequestState()
    data class Success(val response: MutableList<MatchesResponse?>) : ListMatchesRequestState()
    data class Error(val message: String = errorMessage) : ListMatchesRequestState()
}