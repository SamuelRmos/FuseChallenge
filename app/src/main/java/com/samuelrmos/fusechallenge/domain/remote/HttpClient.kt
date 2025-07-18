package com.samuelrmos.fusechallenge.domain.remote

import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import java.io.File

const val BASE_URL = "https://api.pandascore.co/csgo/"

class HttpClient(context: Context) {

    private val cacheSize = (10 * 1024 * 1024).toLong()
    private val cache = Cache(File(context.cacheDir, "http_cache"), cacheSize)

    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .build()
        val newRequest = chain.request()
            .newBuilder()
            .header("Cache-Control", "public, max-age=60")
            .addHeader("Authorization", "Bearer " + BuildConfig.PANDA_KEY_API)
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = BODY
    }

    fun getClient() = if (BuildConfig.DEBUG) {
        OkHttpClient().newBuilder()
            .cache(cache)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient().newBuilder()
            .cache(cache)
            .addInterceptor(authInterceptor)
            .build()
    }
}