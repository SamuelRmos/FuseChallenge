package com.samuelrmos.fusechallenge.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.samuelrmos.fusechallenge.domain.remote.BASE_URL
import com.samuelrmos.fusechallenge.domain.remote.HttpClient
import com.samuelrmos.fusechallenge.domain.remote.PandaApi
import com.samuelrmos.fusechallenge.domain.repository.ListMatchesRepositoryImpl
import com.samuelrmos.fusechallenge.ui.list.ListMatchesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    viewModel { ListMatchesViewModel(get()) }
    factory { HttpClient(get()) }
    single { provideRetrofit(get()) }
    single { ListMatchesRepositoryImpl(get()) }
}

fun provideRetrofit(httpClient: HttpClient): PandaApi = Retrofit.Builder()
    .client(httpClient.getClient())
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()
    .create(PandaApi::class.java)
