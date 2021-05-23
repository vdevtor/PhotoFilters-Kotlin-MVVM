package com.example.photofilterapp.listeners

import com.example.photofilterapp.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}