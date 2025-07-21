package com.samuelrmos.fusechallenge.data.state

import com.samuelrmos.fusechallenge.data.TeamsResponse

sealed class TeamsRequestState {
    data object Loading : TeamsRequestState()
    data class Success(val response: MutableList<TeamsResponse?>) : TeamsRequestState()
    data class Error(val message: String = errorMessage) : TeamsRequestState()
}