package com.dk.nasa.ui.solar.fragments

import android.view.LayoutInflater
import android.view.View
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

    inner class SolarViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photos: Photos) {
            with(binding) {
                if (photos.hide){
                    description.visibility = View.GONE
                    date.visibility = View.GONE
                } else {
                    description.visibility = View.VISIBLE
                    date.visibility = View.VISIBLE
                }
                image.load(photos.image)
                image.setOnClickListener {
                    photos.hide = !photos.hide
                    notifyItemChanged(layoutPosition)
                }
                description.text = photos.description
                date.text = photos.date
            }
        }
    }
}