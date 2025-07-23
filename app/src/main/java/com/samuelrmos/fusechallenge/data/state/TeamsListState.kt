package com.samuelrmos.fusechallenge.data.state

import com.samuelrmos.fusechallenge.data.Players

data class TeamsListState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val playersFirstTeams:  List<Players?>? = null,
    val playersSecondTeams:  List<Players?>? = null
)