package com.samuelrmos.fusechallenge.ui.list

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelrmos.fusechallenge.data.MatchItem
import com.samuelrmos.fusechallenge.data.MatchesResponse
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState
import com.samuelrmos.fusechallenge.data.state.MatchesListState
import com.samuelrmos.fusechallenge.domain.repository.IListMatchesRepository
import com.samuelrmos.fusechallenge.ui.theme.NotRunning
import com.samuelrmos.fusechallenge.ui.theme.Running
import com.samuelrmos.fusechallenge.utils.formatDateTime
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListMatchesViewModel(private val listMatchesRepository: IListMatchesRepository) :
    ViewModel() {

    private val _stateMatchesResponse = MutableStateFlow(MatchesListState())
    val stateMatchesResponse = _stateMatchesResponse.asStateFlow()
    private val sortedMatchesList = mutableListOf<MatchItem>()

    init {
        fetchListMatches()
    }

    @VisibleForTesting
    internal fun fetchListMatches(page: Int = 1) {
        viewModelScope.launch(IO) {
            listMatchesRepository.fetchRunningMatches(page).distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is MatchesListRequestState.Success -> {
                            addRunningMatches(result.response)
                            fetchUpcomingMatches()
                        }

                        is MatchesListRequestState.Loading -> {
                            _stateMatchesResponse.update { it.copy(isLoading = true) }
                        }

                        is MatchesListRequestState.Error -> {
                            _stateMatchesResponse.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun fetchUpcomingMatches(page: Int = 1) {
        viewModelScope.launch(IO) {
            listMatchesRepository.fetchUpcomingMatches(page).distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is MatchesListRequestState.Success -> {
                            addUpcomingMatches(result.response)
                            _stateMatchesResponse.update {
                                it.copy(
                                    isLoading = false,
                                    response = sortedMatchesList
                                )
                            }
                        }

                        is MatchesListRequestState.Loading -> {
                            _stateMatchesResponse.update { it.copy(isLoading = true) }
                        }

                        is MatchesListRequestState.Error -> {
                            _stateMatchesResponse.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                            }
                        }
                    }
                }
        }
    }

    @VisibleForTesting
    internal fun addRunningMatches(matches: MutableList<MatchesResponse?>) {
        matches.forEach {
            it?.games?.forEach { game ->
                if (game.status == "running") {
                    val matchesResponse = MatchItem(
                        game.beginAt?.formatDateTime(),
                        game,
                        it.serie,
                        it.league,
                        it.opponents[0],
                        it.opponents[1],
                    )
                    sortedMatchesList.add(matchesResponse)
                }
            }
        }
    }

    @VisibleForTesting
    internal fun addUpcomingMatches(matches: MutableList<MatchesResponse?>) {
        matches.forEach matches@{
            it?.games?.forEach { game ->
                if (game.status == "not_started" && it.opponents.size == 2) {
                    val matchesResponse = MatchItem(
                        it.beginAt?.formatDateTime(),
                        game,
                        it.serie,
                        it.league,
                        it.opponents[0],
                        it.opponents[1],
                    )
                    sortedMatchesList.add(matchesResponse)
                    return@matches
                }
            }
        }
    }

    fun getComponentColor(gameStatus: String) = if (gameStatus == "running") Running else NotRunning

    fun checkGameStatusAndReturnText(gameStatus: String, gameTime: String): String {
        return if (gameStatus == "running") "AGORA" else gameTime
    }
}