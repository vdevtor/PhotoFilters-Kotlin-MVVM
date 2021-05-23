package com.example.photofilterapp.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photofilterapp.repositories.EditImageRepository
import com.example.photofilterapp.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository): ViewModel() {

    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState


    fun prepareImagePreview(imageUri: Uri){
        Coroutines.io {
            kotlin.runCatching {
                emitImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null){
                    emitImagePreviewUiState(bitmap = bitmap)
                }else{
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
    ){
        val dateState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dateState)
    }

    data class ImagePreviewDataState(
            val isLoading: Boolean,
            val bitmap : Bitmap?,
            val error: String?
    )
}