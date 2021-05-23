package com.example.photofilterapp.di

import com.example.photofilterapp.repositories.EditImageRepository
import com.example.photofilterapp.repositories.EditImageRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository> {EditImageRepositoryImpl(androidContext())  }
}