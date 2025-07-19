package com.samuelrmos.fusechallenge.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState
import com.samuelrmos.fusechallenge.data.state.MatchesListState
import com.samuelrmos.fusechallenge.domain.repository.ListMatchesRepositoryImpl
import com.samuelrmos.fusechallenge.ui.theme.NotRunning
import com.samuelrmos.fusechallenge.ui.theme.Running
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListMatchesViewModel(private val listMatchesRepository: ListMatchesRepositoryImpl) :
    ViewModel() {

    private val _stateMatchesResponse = MutableStateFlow(MatchesListState())
    val stateMatchesResponse = _stateMatchesResponse.asStateFlow()

    init {
        fetchListMatches()
    }

    fun fetchListMatches(page: Int = 1) {
        viewModelScope.launch(IO) {
            listMatchesRepository.fetchRunningMatches(page).distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is MatchesListRequestState.Success -> {
                            _stateMatchesResponse.update {
                                it.copy(
                                    isLoading = false,
                                    response = result.response
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

    fun getComponentColor(gameStatus: String) = if (gameStatus == "running") Running else NotRunning

    fun checkGameStatusAndReturnText(gameStatus: String, gameTime: String): String {
        return if (gameStatus == "running") "AGORA" else gameTime
    }
}