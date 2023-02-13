package com.dk.nasa.ui.solar.fragments

import androidx.recyclerview.widget.DiffUtil
import com.dk.nasa.model.photos.Photos

class SolarPhotosCallback: DiffUtil.ItemCallback<Photos>() {
    override fun areItemsTheSame(oldItem: Photos, newItem: Photos): Boolean {
        return oldItem.hide == newItem.hide
    }
    override fun areContentsTheSame(oldItem: Photos, newItem: Photos): Boolean {
        return oldItem == newItem
    }
}