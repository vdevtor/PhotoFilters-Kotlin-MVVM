package com.example.photofilterapp.activities.editimage

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.photofilterapp.activities.filteredimage.FilteredImageActivity
import com.example.photofilterapp.activities.main.MainActivity
import com.example.photofilterapp.adapters.ImageFiltersAdapter
import com.example.photofilterapp.data.ImageFilter
import com.example.photofilterapp.databinding.ActivityEditImageBinding
import com.example.photofilterapp.listeners.ImageFilterListener
import com.example.photofilterapp.utilities.displayToast
import com.example.photofilterapp.utilities.show
import com.example.photofilterapp.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    companion object {
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }


    private lateinit var binding: ActivityEditImageBinding
    private val viewModel: EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    //image bitmaps
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservables()
        prepareImagePreview()
    }

    private fun setupObservables() {
        viewModel.imagePreviewUiState.observe(this, {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                    if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                // for the first time filtered image = original image
                originalBitmap = bitmap
                filteredBitmap.value = bitmap
                with(originalBitmap) {
                    gpuImage.setImage(this)
                    binding.imagePreView.show()
                    viewModel.loadImageFilters(this)
                }
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
        viewModel.imageFiltersUiState.observe(this, {
            val imageFilterDataState = it ?: return@observe
            binding.imageFiltersProgressBar.visibility =
                    if (imageFilterDataState.isLoading) View.VISIBLE else View.GONE
            imageFilterDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFilterDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
        filteredBitmap.observe(this, { bitmap ->
            binding.imagePreView.setImageBitmap(bitmap)
        })
        viewModel.saveFilteredImageUiState.observe(this, {
            val savedFilteredImageDataState = it ?: return@observe
            if (savedFilteredImageDataState.isLoading) {
                binding.imageSave.visibility = View.GONE
                binding.savingProgressBar.visibility = View.VISIBLE

            } else {
                binding.savingProgressBar.visibility = View.GONE
                binding.imageSave.visibility = View.VISIBLE
            }
            savedFilteredImageDataState.uri?.let { savedImageUri ->
                Intent(
                        applicationContext,
                        FilteredImageActivity::class.java
                ).also { filterImageIntent ->
                    filterImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
                    startActivity(filterImageIntent)
                }
            } ?: kotlin.run {
                savedFilteredImageDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)

        }
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            filteredBitmap.value?.let{ bitmap ->
                viewModel.saveFilteredImage(bitmap)
            }
        }

        binding.imagePreView.setOnLongClickListener {
            binding.imagePreView.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreView.setOnClickListener {
            binding.imagePreView.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}