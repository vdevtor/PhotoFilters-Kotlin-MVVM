package com.example.photofilterapp.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.util.ArrayList

class SavedImagesRepositoryImpl(private val context: Context) : SavedImagesRepository {
    override suspend fun loadSavedImage(): List<Pair<File, Bitmap>>? {
        val savedImages = ArrayList<Pair<File, Bitmap>>()
        val dir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Saved Images"
        )
        dir.listFiles()?.let { data ->
            data.forEach { file ->
                savedImages.add(Pair(file, getPreviewBitmap(file)))
            }
            return savedImages
        } ?: return null
    }

    private fun getPreviewBitmap(file: File): Bitmap {
        val originalBitmap = BitmapFactory.decodeFile(file.absolutePath)
        val width = 150
        val height = ((originalBitmap.height * width) / originalBitmap.width)
        return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
    }

}