package com.samuelrmos.fusechallenge.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelrmos.fusechallenge.data.TeamsResponse
import com.samuelrmos.fusechallenge.data.state.TeamsListState
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState
import com.samuelrmos.fusechallenge.domain.repository.ITeamsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsScreenViewModel(private val teamsRepository: ITeamsRepository) : ViewModel() {

    private val _stateTeamsResponse = MutableStateFlow(TeamsListState())
    val stateTeamsResponse = _stateTeamsResponse.asStateFlow()

    fun fetchPlayers(firstTeamName: String, secondTeamName: String) {
        viewModelScope.launch(IO) {
            teamsRepository.fetchTeams().distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is TeamsRequestState.Success -> {
                            fetchPlayers(firstTeamName, secondTeamName, result.response)
                        }

                        is TeamsRequestState.Loading -> {
                            _stateTeamsResponse.update { it.copy(isLoading = true) }
                        }

                        is TeamsRequestState.Error -> {
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

    private fun fetchPlayers(
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