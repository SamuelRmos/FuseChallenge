package com.samuelrmos.fusechallenge.domain.repository

import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState
import kotlinx.coroutines.flow.Flow

interface IListMatchesRepository {
    fun fetchRunningMatches(page: Int) : Flow<MatchesListRequestState>

    fun fetchUpcomingMatches(page: Int) : Flow<MatchesListRequestState>
}