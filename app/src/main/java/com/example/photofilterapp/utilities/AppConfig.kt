package com.example.photofilterapp.utilities

import android.app.Application
import com.example.photofilterapp.di.repositoryModule
import com.example.photofilterapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class AppConfig: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}