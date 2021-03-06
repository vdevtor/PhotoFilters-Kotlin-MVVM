package com.example.photofilterapp.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.photofilterapp.databinding.ItemContainerSavedImageBinding
import com.example.photofilterapp.listeners.SavedImageListener
import java.io.File

class SavedImageAdapter(
        private val savedImages: List<Pair<File, Bitmap>>,
        private val savedImageListener: SavedImageListener
        ) : RecyclerView.Adapter<SavedImageAdapter.SavedImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
        val binding = ItemContainerSavedImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
        )
        return SavedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
        with(holder) {
            with(savedImages[position]) {
                binding.imageSave.setImageBitmap(second)
                binding.imageSave.setOnClickListener {
                    savedImageListener.onImageClick(first)
                }
            }
        }
    }

    override fun getItemCount() = savedImages.size

    inner class SavedImageViewHolder(val binding: ItemContainerSavedImageBinding)
        : RecyclerView.ViewHolder(binding.root)

}