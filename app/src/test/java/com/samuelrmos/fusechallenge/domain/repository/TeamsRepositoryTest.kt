package com.samuelrmos.fusechallenge.domain.repository

import com.samuelrmos.fusechallenge.data.TeamsResponse
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Error
import com.samuelrmos.fusechallenge.data.state.TeamsRequestState.Loading
import com.samuelrmos.fusechallenge.domain.remote.PandaApi
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class TeamsRepositoryTest {

    @MockK
    private lateinit var pandaApi: PandaApi

    private lateinit var repository: ITeamsRepository

    @Before
    fun setup() {
        init(this, relaxed = true)
        repository = spyk(TeamsRepositoryImpl(pandaApi))
    }

    @Test
    fun `fetch teams should returns error when received error`() = runTest {
        val expectedValues = listOf(Loading, Error())
        val collectedStates = mutableListOf<TeamsRequestState>()
        coEvery { pandaApi.fetchTeams() } returns Response.error(
            404,
            "User not found".toResponseBody("application/json".toMediaTypeOrNull())
        )

        repository.fetchTeams().collect {
            collectedStates.add(it)
        }

        assertEquals(expectedValues, collectedStates)
    }

    @Test
    fun `fetch teams should returns error calls api throws exception`() = runTest {
        val expectedValues = listOf(Loading, Error())
        val collectedStates = mutableListOf<TeamsRequestState>()
        coEvery { pandaApi.fetchTeams() } throws Exception()

        repository.fetchTeams().collect {
            collectedStates.add(it)
        }

        assertEquals(expectedValues, collectedStates)
    }

    @Test
    fun `fetch teams should returns success when received success`() = runTest {
        val collectedStates = mutableListOf<TeamsRequestState>()
        val response = mockk<MutableList<TeamsResponse?>>()
        coEvery { pandaApi.fetchTeams() } returns Response.success(response)

        repository.fetchTeams().collect {
            collectedStates.add(it)
        }

        val teamResponse = (collectedStates[1] as TeamsRequestState.Success).response

        assertEquals(response, teamResponse)
    }
}