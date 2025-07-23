package com.samuelrmos.fusechallenge.domain.repository

import com.samuelrmos.fusechallenge.data.MatchesResponse
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState.Error
import com.samuelrmos.fusechallenge.data.state.MatchesListRequestState.Loading
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

class ListMatchesRepositoryTest {

    @MockK
    private lateinit var pandaApi: PandaApi

    private lateinit var repository: IListMatchesRepository

    @Before
    fun setup() {
        init(this, relaxed = true)
        repository = spyk(ListMatchesRepositoryImpl(pandaApi))
    }

    @Test
    fun `fetch running matches should returns error when received error`() = runTest {
        val expectedValues = listOf(Loading, Error())
        val collectedStates = mutableListOf<MatchesListRequestState>()
        coEvery { pandaApi.fetchRunningMatches(1) } returns Response.error(
            404,
            "User not found".toResponseBody("application/json".toMediaTypeOrNull())
        )

        repository.fetchRunningMatches(1).collect {
            collectedStates.add(it)
        }

        assertEquals(expectedValues, collectedStates)
    }

    @Test
    fun `fetch running matches should returns error calls api throws exception`() = runTest {
        val expectedValues = listOf(Loading, Error())
        val collectedStates = mutableListOf<MatchesListRequestState>()
        coEvery { pandaApi.fetchRunningMatches(1) } throws Exception()

        repository.fetchRunningMatches(1).collect {
            collectedStates.add(it)
        }

        assertEquals(expectedValues, collectedStates)
    }

    @Test
    fun `fetch running matches should returns success when received success`() = runTest {
        val collectedStates = mutableListOf<MatchesListRequestState>()
        val response = mockk<MutableList<MatchesResponse?>>()
        coEvery { pandaApi.fetchRunningMatches(1) } returns Response.success(response)

        repository.fetchRunningMatches(1).collect {
            collectedStates.add(it)
        }

        val teamResponse = (collectedStates[1] as MatchesListRequestState.Success).response

        assertEquals(response, teamResponse)
    }
}