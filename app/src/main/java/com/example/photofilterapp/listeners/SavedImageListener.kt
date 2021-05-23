package com.example.photofilterapp.listeners

import java.io.File

interface SavedImageListener {
    fun onImageClick(file:File)
}