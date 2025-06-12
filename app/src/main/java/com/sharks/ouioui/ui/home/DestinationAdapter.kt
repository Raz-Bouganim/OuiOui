package com.sharks.ouioui.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharks.ouioui.R
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.databinding.ItemDestinationBinding

class DestinationAdapter(private var destinations: List<Destination>) : RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    inner class DestinationViewHolder(private val binding: ItemDestinationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(destination: Destination) {
            binding.textViewDestinationName.text = destination.title
            binding.textViewDestinationDescription.text = destination.description
            Glide.with(binding.imageViewDestinationThumbnail.context)
                .load(destination.thumbnail)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.imageViewDestinationThumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDestinationBinding.inflate(inflater, parent, false)
        return DestinationViewHolder(binding)
    }

    override fun getItemCount(): Int = destinations.size

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(destinations[position])
    }

    fun updateData(newDestinations: List<Destination>) {
        this.destinations = newDestinations
        notifyDataSetChanged()
    }
}