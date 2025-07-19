package com.samuelrmos.fusechallenge.domain.repository

import android.util.Log
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState.Error
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState.Loading
import com.samuelrmos.fusechallenge.data.state.errorMessage
import com.samuelrmos.fusechallenge.domain.remote.PandaApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ListMatchesRepositoryImpl(private val pandaApi: PandaApi) : IListMatchesRepository {

    override fun fetchRunningMatches(page: Int): Flow<MatchesListRequestState> = flow {
        kotlin.runCatching {
            emit(Loading)
            pandaApi.fetchRunningMatches(page).run {
                body()?.let {
                    if (isSuccessful) {
                        emit(MatchesListRequestState.Success(it))
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

    override fun fetchUpcomingMatches(page: Int): Flow<MatchesListRequestState> = flow {
        kotlin.runCatching {
            emit(Loading)
            pandaApi.fetchUpcomingMatches(page).run {
                body()?.let {
                    if (isSuccessful) {
                        emit(MatchesListRequestState.Success(it))
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