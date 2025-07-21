package com.samuelrmos.fusechallenge.domain.remote

import com.samuelrmos.fusechallenge.data.MatchesResponse
import com.samuelrmos.fusechallenge.data.TeamsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PandaApi {

    @GET("matches/running")
    suspend fun fetchRunningMatches(
        @Query("page") page: Int
    ): Response<MutableList<MatchesResponse?>>

    @GET("matches/upcoming")
    suspend fun fetchUpcomingMatches(
        @Query("page") page: Int
    ): Response<MutableList<MatchesResponse?>>

    @GET("teams")
    suspend fun fetchTeams(@Query("name") perPage: Int = 100): Response<MutableList<TeamsResponse?>>
}