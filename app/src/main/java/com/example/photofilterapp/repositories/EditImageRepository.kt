package com.example.photofilterapp.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.example.photofilterapp.data.ImageFilter

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri : Uri) : Bitmap?
    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>
    suspend fun savedFilteredImage(filteredBitmap: Bitmap): Uri?
}