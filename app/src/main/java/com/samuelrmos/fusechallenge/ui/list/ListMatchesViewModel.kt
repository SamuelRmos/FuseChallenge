package com.samuelrmos.fusechallenge.ui.list

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelrmos.fusechallenge.data.MatchItem
import com.samuelrmos.fusechallenge.data.MatchesResponse
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState.Error
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState.Loading
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState.Success
import com.samuelrmos.fusechallenge.data.state.MatchesListState
import com.samuelrmos.fusechallenge.data.state.PaginationState
import com.samuelrmos.fusechallenge.domain.repository.IListMatchesRepository
import com.samuelrmos.fusechallenge.ui.theme.NotRunning
import com.samuelrmos.fusechallenge.ui.theme.Running
import com.samuelrmos.fusechallenge.utils.formatDateTime
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MATCHES_LIMIT = 300

private const val RUNNING = "running"

private const val NOW = "AGORA"

private const val NOT_STARTED = "not_started"

class ListMatchesViewModel(private val listMatchesRepository: IListMatchesRepository) :
    ViewModel() {

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    private val _stateMatchesResponse = MutableStateFlow(MatchesListState())
    val stateMatchesResponse = _stateMatchesResponse.asStateFlow()

    private val _paginationRatedState = MutableStateFlow(PaginationState())

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
                        is Success -> {
                            addRunningMatches(result.response)
                            fetchUpcomingMatches(page)
                        }

                        is Loading -> {
                            _stateMatchesResponse.update { it.copy(isLoading = true) }
                        }

                        is Error -> {
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
    internal fun fetchUpcomingMatches(page: Int = 1) {
        viewModelScope.launch(IO) {
            listMatchesRepository.fetchUpcomingMatches(page).distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is Success -> {
                            addUpcomingMatches(result.response)
                            _stateMatchesResponse.update {
                                it.copy(
                                    isLoading = false,
                                    response = sortedMatchesList.sortedByDescending { matchItem ->
                                        matchItem.game.status == RUNNING
                                    }
                                )
                            }
                            updatePagination()
                        }

                        is Loading -> {
                            _stateMatchesResponse.update { it.copy(isLoading = true) }
                        }

                        is Error -> {
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

    private fun updatePagination() {
        _stateMatchesResponse.value.response?.size?.let { listSize ->
            _paginationRatedState.update {
                it.copy(
                    skip = it.skip + 1,
                    isEndReached = listSize >= MATCHES_LIMIT,
                    isLoading = false
                )
            }
        }
    }

    @VisibleForTesting
    internal fun addRunningMatches(matches: MutableList<MatchesResponse?>) {
        matches.forEach {
            it?.games?.forEach { game ->
                if (game.status == RUNNING) {
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
                if (game.status == NOT_STARTED && it.opponents.size == 2) {
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

    fun getComponentColor(gameStatus: String) = if (gameStatus == RUNNING) Running else NotRunning

    fun checkGameStatusAndReturnText(gameStatus: String, gameTime: String): String {
        return if (gameStatus == RUNNING) NOW else gameTime
    }

    fun refresh() {
        viewModelScope.launch(IO) {
            updateRefreshState(true)
            if (_paginationRatedState.value.isEndReached.not()) {
                fetchListMatches(_paginationRatedState.value.skip)
            }
            updateRefreshState(false)
        }
    }

    private fun updateRefreshState(value: Boolean) = _isRefresh.update { value }
}