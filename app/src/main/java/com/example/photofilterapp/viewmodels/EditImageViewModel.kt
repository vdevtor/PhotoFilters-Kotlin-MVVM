package com.example.photofilterapp.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photofilterapp.data.ImageFilter
import com.example.photofilterapp.repositories.EditImageRepository
import com.example.photofilterapp.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository) : ViewModel() {
    //region:: Prepare image view
    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState


    fun prepareImagePreview(imageUri: Uri) {
        Coroutines.io {
            kotlin.runCatching {
                emitImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null) {
                    emitImagePreviewUiState(bitmap = bitmap)
                } else {
                    emitImagePreviewUiState(error = "Unable to prepare image preview")
                }
            }.onFailure {
                emitImagePreviewUiState(error = it.message.toString())
            }
        }
    }

    private fun emitImagePreviewUiState(
            isLoading: Boolean = false,
            bitmap: Bitmap? = null,
            error: String? = null
    ) {
        val dateState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dateState)
    }

    data class ImagePreviewDataState(
            val isLoading: Boolean,
            val bitmap: Bitmap?,
            val error: String?
    )
    //::endregion

    //region:: Load image filters
    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()
    val imageFiltersUiState: LiveData<ImageFiltersDataState> get() = imageFiltersDataState

    fun loadImageFilters(originalImage: Bitmap) {
        Coroutines.io {
            kotlin.runCatching {
                emitImageFiltersUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess { imageFilters ->
                emitImageFiltersUiState(imageFilters = imageFilters)
            }.onFailure {
                emitImageFiltersUiState(error = it.message.toString())
            }
        }
    }

    private fun getPreviewImage(originalImage: Bitmap): Bitmap {
        return kotlin.runCatching {
            val previewWidth = 150
            val previewHeight = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage, previewWidth, previewHeight, false)
        }.getOrDefault(originalImage)
    }

    private fun emitImageFiltersUiState(
            isLoading: Boolean = false,
            imageFilters: List<ImageFilter>? = null,
            error: String? = null
    ) {
        val dataState = ImageFiltersDataState(isLoading, imageFilters, error)
        imageFiltersDataState.postValue(dataState)
    }

    data class ImageFiltersDataState(
            val isLoading: Boolean,
            val imageFilters: List<ImageFilter>?,
            val error: String?
    )
//endregion

    //region Save filtered Image
    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUiState: LiveData<SaveFilteredImageDataState> get() = saveFilteredImageDataState


    fun saveFilteredImage(filteredBitmap: Bitmap) {
        Coroutines.io {
            kotlin.runCatching {
                emitSavedFilteredImage(isLoading = true)
                editImageRepository.savedFilteredImage(filteredBitmap)
            }.onSuccess { savedImageUri ->
                emitSavedFilteredImage(uri = savedImageUri)
            }.onFailure {
                emitSavedFilteredImage(error = it.message.toString())
            }
        }
    }

    private fun emitSavedFilteredImage(
            isLoading: Boolean = false,
            uri: Uri? = null,
            error: String? = null
    ) {
        val dataState = SaveFilteredImageDataState(isLoading, uri, error)
        saveFilteredImageDataState.postValue(dataState)
    }

    data class SaveFilteredImageDataState(
            val isLoading: Boolean,
            val uri: Uri?,
            val error: String?
    )
    //endregion
}