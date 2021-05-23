package com.example.photofilterapp.repositories

import android.graphics.Bitmap
import android.net.Uri

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri : Uri) : Bitmap?
}