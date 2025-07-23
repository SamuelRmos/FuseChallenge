package com.samuelrmos.fusechallenge.domain.repository

import com.samuelrmos.fusechallenge.data.state.TeamsRequestState
import kotlinx.coroutines.flow.Flow

interface ITeamsRepository {
    fun fetchTeams(): Flow<TeamsRequestState>
}