package com.samuelrmos.fusechallenge.ui.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelrmos.fusechallenge.data.MatchItem
import com.samuelrmos.fusechallenge.data.TeamsResponse
import com.samuelrmos.fusechallenge.data.state.TeamsListState
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Error
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Loading
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Success
import com.samuelrmos.fusechallenge.domain.repository.ITeamsRepository
import com.samuelrmos.fusechallenge.navigation.Screens.DetailScreen
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class DetailsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val teamsRepository: ITeamsRepository
) : ViewModel() {

    private val matchItem = savedStateHandle.get<MatchItem>(DetailScreen.route)
    private val _stateTeamsResponse = MutableStateFlow(TeamsListState())
    val stateTeamsResponse = _stateTeamsResponse.asStateFlow()

    init {
        val firstTeamName = matchItem?.firstOpponent?.opponentDescriptions?.name.orEmpty()
        val secondTeamName = matchItem?.secondOpponent?.opponentDescriptions?.name.orEmpty()
        fetchPlayers(firstTeamName, secondTeamName)
    }

    private fun fetchPlayers(firstTeamName: String, secondTeamName: String) {
        viewModelScope.launch(IO) {
            teamsRepository.fetchTeams().distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is Success -> {
                            fetchPlayers(firstTeamName, secondTeamName, result.response)
                        }

                        is Loading -> {
                            _stateTeamsResponse.update { it.copy(isLoading = true) }
                        }

                        is Error -> {
                            _stateTeamsResponse.update {
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
    internal fun fetchPlayers(
        firstTeamName: String,
        secondTeamName: String,
        teamsList: MutableList<TeamsResponse?>
    ) {
        teamsList.forEach { team ->
            if (team?.teamName == firstTeamName) {
                _stateTeamsResponse.update {
                    it.copy(
                        playersFirstTeams = team.players
                    )
                }
            }

            if (team?.teamName == secondTeamName) {
                _stateTeamsResponse.update {
                    it.copy(
                        playersSecondTeams = team.players
                    )
                }
            }
        }

        _stateTeamsResponse.update {
            it.copy(isLoading = false)
        }
    }
}