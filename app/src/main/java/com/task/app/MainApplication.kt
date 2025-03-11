package com.task.app

import android.app.Application
import com.task.feature_gifs.domain.koin.koinGifsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(koinGifsModule)
        }
    }
}