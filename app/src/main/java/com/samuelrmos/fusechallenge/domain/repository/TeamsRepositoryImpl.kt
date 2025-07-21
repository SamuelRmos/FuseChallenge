package com.samuelrmos.fusechallenge.domain.repository

import com.samuelrmos.fusechallenge.data.state.TeamsRequestState
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Error
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Loading
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Success
import com.samuelrmos.fusechallenge.data.state.errorMessage
import com.samuelrmos.fusechallenge.domain.remote.PandaApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TeamsRepositoryImpl(private val pandaApi: PandaApi) : ITeamsRepository {

    override fun fetchTeams(): Flow<TeamsRequestState> = flow {
        kotlin.runCatching {
            emit(Loading)
            pandaApi.fetchTeams().run {
                body()?.let {
                    if (isSuccessful) {
                        emit(Success(it))
                    } else {
                        emit(Error())
                    }
                } ?: run {
                    emit(Error())
                }
            }
        }.onFailure {
            emit(Error(it.message ?: errorMessage))
        }
    }.flowOn(IO)
}