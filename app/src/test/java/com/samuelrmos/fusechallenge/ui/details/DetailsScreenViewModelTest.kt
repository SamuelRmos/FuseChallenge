package com.samuelrmos.fusechallenge.ui.details

import androidx.lifecycle.SavedStateHandle
import com.samuelrmos.fusechallenge.data.MatchItem
import com.samuelrmos.fusechallenge.data.OpponentDescriptions
import com.samuelrmos.fusechallenge.data.Opponents
import com.samuelrmos.fusechallenge.data.Players
import com.samuelrmos.fusechallenge.data.TeamsResponse
import com.samuelrmos.fusechallenge.data.state.TeamsListState
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Error
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Success
import com.samuelrmos.fusechallenge.domain.repository.ITeamsRepository
import com.samuelrmos.fusechallenge.navigation.Screens
import io.mockk.MockKAnnotations.init
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DetailsScreenViewModelTest {

    @MockK
    private lateinit var teamsRepository: ITeamsRepository

    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        init(this, relaxed = true)
        setupSavedStateHandle()
    }

    private fun setupSavedStateHandle() {
        val teamA = "Team A"
        val teamB = "Team B"
        val matchItem = MatchItem(
            beginAt = null,
            game = mockk(),
            serie = mockk(),
            league = mockk(),
            firstOpponent = Opponents(
                opponentDescriptions = OpponentDescriptions(null, teamA),
                type = ""
            ),
            secondOpponent = Opponents(
                opponentDescriptions = OpponentDescriptions(null, teamB),
                type = ""
            )
        )
        savedStateHandle = SavedStateHandle(mapOf(Screens.DetailScreen.route to matchItem))
    }


    @Test
    fun `fetch players should calls repository fetch teams and fetch players when success`() =
        runTest {
            val response = mockk<MutableList<TeamsResponse?>>()
            val teamA = "Team A"
            val teamB = "Team B"

            every { teamsRepository.fetchTeams() } returns flowOf(Success(response))

            val detailsScreenViewModel = DetailsScreenViewModel(savedStateHandle, teamsRepository)

            coVerify { detailsScreenViewModel.fetchPlayers(teamA, teamB, response) }
        }

    @Test
    fun `fetch players should returns state error when received error`() = runTest {
        val emittedStates = mutableListOf<TeamsListState>()
        val response = mockk<MutableList<TeamsResponse?>>()
        val teamA = "Team A"
        val teamB = "Team B"

        every { teamsRepository.fetchTeams() } returns flow {
            emit(Error())
        }

        val detailsScreenViewModel = DetailsScreenViewModel(savedStateHandle, teamsRepository)

        detailsScreenViewModel.stateTeamsResponse
            .take(2)
            .onEach { emittedStates.add(it) }
            .collect()

        val state = emittedStates[1]
        assertFalse(state.isLoading)
        assertEquals("Something went wrong", state.errorMessage)
        assertNull(state.playersFirstTeams)
        assertNull(state.playersSecondTeams)
        coVerify(inverse = true) { detailsScreenViewModel.fetchPlayers(teamA, teamB, response) }
    }

    @Test
    fun `fetch players should returns state loading when received state loading`() = runTest {
        val emittedStates = mutableListOf<TeamsListState>()
        val response = mockk<MutableList<TeamsResponse?>>()
        val teamA = "Team A"
        val teamB = "Team B"

        every { teamsRepository.fetchTeams() } returns flowOf(TeamsRequestState.Loading)

        val detailsScreenViewModel = DetailsScreenViewModel(savedStateHandle, teamsRepository)

        detailsScreenViewModel.stateTeamsResponse
            .take(1)
            .onEach { emittedStates.add(it) }
            .collect()

        val state = emittedStates[0]
        assertTrue(state.isLoading)
        assertTrue(state.errorMessage.isNullOrEmpty())
        assertNull(state.playersFirstTeams)
        assertNull(state.playersSecondTeams)
        coVerify(inverse = true) { detailsScreenViewModel.fetchPlayers(teamA, teamB, response) }
    }

    @Test
    fun `fetch players should returns state with team players when first team name contains on received list`() =
        runTest {
            val teamA = "Team A"
            val player = mockk<Players>()
            val listPlayers = listOf(player)
            val emittedStates = mutableListOf<TeamsListState>()
            val response = mutableListOf<TeamsResponse?>(TeamsResponse(teamA, listPlayers))
            every { teamsRepository.fetchTeams() } returns flowOf(Success(response))

            val detailsScreenViewModel = DetailsScreenViewModel(savedStateHandle, teamsRepository)

            detailsScreenViewModel.stateTeamsResponse
                .take(2)
                .onEach { emittedStates.add(it) }
                .collect()
            val state = emittedStates[1]

            state.run {
                assertFalse(isLoading)
                assertTrue(errorMessage.isNullOrEmpty())
                assertEquals(listPlayers, this.playersFirstTeams)
            }
        }

    @Test
    fun `fetch players should returns state with team players when second team name contains on received list`() =
        runTest {
            val teamB = "Team B"
            val player = mockk<Players>()
            val listPlayers = listOf(player)
            val emittedStates = mutableListOf<TeamsListState>()
            val response = mutableListOf<TeamsResponse?>(TeamsResponse(teamB, listPlayers))

            every { teamsRepository.fetchTeams() } returns flowOf(Success(response))

            val detailsScreenViewModel = DetailsScreenViewModel(savedStateHandle, teamsRepository)

            detailsScreenViewModel.stateTeamsResponse
                .take(2)
                .onEach { emittedStates.add(it) }
                .collect()
            val state = emittedStates[1]

            state.run {
                assertFalse(isLoading)
                assertTrue(errorMessage.isNullOrEmpty())
                assertEquals(listPlayers, this.playersSecondTeams)
            }
        }
}