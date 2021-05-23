package com.example.photofilterapp.activities.editimage

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.photofilterapp.activities.main.MainActivity
import com.example.photofilterapp.databinding.ActivityEditImageBinding
import com.example.photofilterapp.utilities.displayToast
import com.example.photofilterapp.utilities.show
import com.example.photofilterapp.viewmodels.EditImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditImageBinding
    private val viewModel: EditImageViewModel by viewModel()
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
                binding.imagePreView.setImageBitmap(bitmap)
                binding.imagePreView.show()
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    fun prepareImagePreview() {
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)

        }
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }
}