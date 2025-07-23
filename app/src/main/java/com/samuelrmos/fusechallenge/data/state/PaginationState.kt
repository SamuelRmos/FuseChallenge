package com.samuelrmos.fusechallenge.data.state

data class PaginationState(
    val isLoading: Boolean = false,
    val skip: Int = 2,
    val isEndReached: Boolean = false
)