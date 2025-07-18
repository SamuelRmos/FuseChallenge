package com.samuelrmos.fusechallenge

import android.app.Application
import com.samuelrmos.fusechallenge.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FuseChallengeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FuseChallengeApplication)
            modules(appModule)
        }
    }
}