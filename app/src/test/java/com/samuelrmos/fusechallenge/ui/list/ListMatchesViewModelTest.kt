package com.samuelrmos.fusechallenge.ui.list

import com.samuelrmos.fusechallenge.data.Game
import com.samuelrmos.fusechallenge.data.League
import com.samuelrmos.fusechallenge.data.MatchesResponse
import com.samuelrmos.fusechallenge.data.Opponents
import com.samuelrmos.fusechallenge.data.Serie
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState.Success
import com.samuelrmos.fusechallenge.data.state.MatchesListState
import com.samuelrmos.fusechallenge.domain.repository.IListMatchesRepository
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

class ListMatchesViewModelTest {

    @MockK
    private lateinit var repository: IListMatchesRepository

    private lateinit var viewModel: ListMatchesViewModel

    @Before
    fun setup() {
        init(this, relaxed = true)
    }

    @Test
    fun `fetch running matches should calls repository fetch running matches add running matches and fetch upcoming matches when success`() =
        runTest {
            val emittedStates = mutableListOf<MatchesListState>()
            val gameRunning = mockk<Game>(relaxed = true)
            val gameUpcoming = mockk<Game>(relaxed = true)
            val league = mockk<League>()
            val opponent1 = mockk<Opponents>()
            val opponent2 = mockk<Opponents>()
            val opponentsList = listOf(opponent1, opponent2)
            val gameListRunning = listOf(gameRunning)
            val gameListUpcoming = listOf(gameUpcoming)

            val matchesRunning = MatchesResponse(
                "2025-07-22T02:31:59Z",
                gameListRunning,
                mockk<Serie>(),
                league,
                opponentsList
            )

            val matchesUpcoming = MatchesResponse(
                "2025-07-22T02:31:59Z",
                gameListUpcoming,
                mockk<Serie>(),
                league,
                opponentsList
            )

            every { gameRunning.status } returns "running"
            every { gameRunning.beginAt } returns "2025-07-22T02:31:59Z"
            every { gameUpcoming.status } returns "not_started"
            every { repository.fetchRunningMatches(1) } returns flowOf(
                Success(mutableListOf(matchesRunning))
            )
            every { repository.fetchUpcomingMatches(1) } returns flowOf(
                Success(mutableListOf(matchesUpcoming))
            )

            viewModel = ListMatchesViewModel(repository)

            viewModel.stateMatchesResponse
                .take(2)
                .onEach { emittedStates.add(it) }
                .collect()

            val state = emittedStates[1]
            state.run {
                assertFalse(isLoading)
                assertTrue(errorMessage.isNullOrEmpty())
                assertTrue(state.response.isNullOrEmpty().not())
            }
        }

    @Test
    fun `fetch running matches should returns state error when received error`() = runTest {
        val emittedStates = mutableListOf<MatchesListState>()

        every { repository.fetchRunningMatches(1) } returns flow {
            emit(MatchesListRequestState.Error())
        }

        viewModel = ListMatchesViewModel(repository)

        viewModel.stateMatchesResponse
            .take(2)
            .onEach { emittedStates.add(it) }
            .collect()

        val state = emittedStates[1]
        assertFalse(state.isLoading)
        assertEquals("Something went wrong", state.errorMessage)
        assertNull(state.response)
    }

    @Test
    fun `fetch running matches should returns state loading when received loading`() = runTest {
        val emittedStates = mutableListOf<MatchesListState>()
        val response = mockk<MutableList<MatchesResponse?>>()

        every { repository.fetchRunningMatches(1) } returns flow {
            emit(MatchesListRequestState.Error())
        }

        viewModel = ListMatchesViewModel(repository)

        viewModel.stateMatchesResponse
            .take(1)
            .onEach { emittedStates.add(it) }
            .collect()

        val state = emittedStates[0]
        assertTrue(state.isLoading)
        assertTrue(state.errorMessage.isNullOrEmpty())
        assertNull(state.response)
        coVerify(inverse = true) {
            viewModel.run {
                addRunningMatches(response)
                fetchUpcomingMatches()
            }
        }
    }
}