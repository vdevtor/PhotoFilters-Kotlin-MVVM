package com.example.photofilterapp.di

import com.example.photofilterapp.viewmodels.EditImageViewModel
import com.example.photofilterapp.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}