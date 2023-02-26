package com.dk.nasa.ui.solar.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dk.nasa.databinding.ItemPhotoBinding
import com.dk.nasa.model.photos.Photos

class SolarAdapter(): ListAdapter<Photos, SolarAdapter.SolarViewHolder>(SolarPhotosCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolarViewHolder {
        val binding =
            ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SolarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SolarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SolarViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photos: Photos) {
            with(binding){
                image.load(photos.image)
                description.text = photos.description
                date.text = photos.date
            }
        }
    }
}