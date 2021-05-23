package com.example.photofilterapp.activities.savedimages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.example.photofilterapp.activities.editimage.EditImageActivity
import com.example.photofilterapp.activities.filteredimage.FilteredImageActivity
import com.example.photofilterapp.adapters.SavedImageAdapter
import com.example.photofilterapp.databinding.ActivitySavedImagesBinding
import com.example.photofilterapp.listeners.SavedImageListener
import com.example.photofilterapp.utilities.displayToast
import com.example.photofilterapp.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImagesActivity : AppCompatActivity(), SavedImageListener {
    private lateinit var binding: ActivitySavedImagesBinding
    private val viewModel: SavedImagesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObserver()
        setListeners()
        viewModel.loadSavedImages()
    }

    private fun setObserver() {
        viewModel.savedImagesUiState.observe(this, {
            val savedImagesDataState = it ?: return@observe
            binding.savedImagesProgressBar.visibility =
                    if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImages?.let { savedImages ->
                SavedImageAdapter(savedImages,this).also {adapter ->
                    with(binding.savedImagesRecyclerView){
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: kotlin.run {
                savedImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    private fun setListeners(){
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onImageClick(file: File) {
        val fileUri = FileProvider.getUriForFile(
                applicationContext,
                "${packageName}.provider",
                file
        )
        Intent(
                applicationContext,
                FilteredImageActivity::class.java
        ).also { filteredImageIntent ->
            filteredImageIntent.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI,fileUri)
            startActivity(filteredImageIntent)
        }
    }
}