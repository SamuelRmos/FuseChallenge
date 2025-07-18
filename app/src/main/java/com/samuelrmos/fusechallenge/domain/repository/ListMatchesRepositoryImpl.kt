package com.samuelrmos.fusechallenge.domain.repository

import com.samuelrmos.fusechallenge.data.state.ListMatchesRequestState
import com.samuelrmos.fusechallenge.data.state.ListMatchesRequestState.Error
import com.samuelrmos.fusechallenge.data.state.ListMatchesRequestState.Loading
import com.samuelrmos.fusechallenge.data.state.errorMessage
import com.samuelrmos.fusechallenge.domain.remote.PandaApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ListMatchesRepositoryImpl(private val pandaApi: PandaApi) : IListMatchesRepository {

    override fun fetchRunningMatches(page: Int): Flow<ListMatchesRequestState> = flow {
        kotlin.runCatching {
            emit(Loading)
            pandaApi.fetchRunningMatches(page).run {
                body()?.let {
                    if (isSuccessful) {
                        emit(ListMatchesRequestState.Success(it))
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
    }

}