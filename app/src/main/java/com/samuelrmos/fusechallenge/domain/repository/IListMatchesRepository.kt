package com.samuelrmos.fusechallenge.domain.repository

import com.samuelrmos.fusechallenge.data.state.ListMatchesRequestState
import kotlinx.coroutines.flow.Flow

interface IListMatchesRepository {
    fun fetchRunningMatches(page: Int) : Flow<ListMatchesRequestState>
}