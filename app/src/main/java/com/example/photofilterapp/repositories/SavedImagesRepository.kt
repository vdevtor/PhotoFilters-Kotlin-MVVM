package com.example.photofilterapp.repositories

import android.graphics.Bitmap
import java.io.File

interface SavedImagesRepository {
    suspend fun loadSavedImage(): List<Pair<File,Bitmap>>?
}