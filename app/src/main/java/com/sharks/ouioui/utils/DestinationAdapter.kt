package com.sharks.ouioui.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharks.ouioui.R
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.databinding.ItemDestinationBinding

class DestinationAdapter(
    private var destinations: List<Destination>,
    private val onFavoriteClick: (Destination, Int)-> Unit,
    private val isFavorite: (Destination) -> Boolean
) : RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    inner class DestinationViewHolder(private val binding: ItemDestinationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(destination: Destination, position: Int) {
            binding.textViewDestinationName.text = destination.title
            binding.textViewDestinationDescription.text = destination.description
            Glide.with(binding.imageViewDestinationThumbnail.context)
                .load(destination.thumbnail)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.imageViewDestinationThumbnail)

            val favorite = isFavorite(destination)
            binding.buttonFavorite.setImageResource(
                if (favorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            )
            binding.buttonFavorite.setColorFilter(
                if (favorite) Color.RED else Color.WHITE
            )

            binding.buttonFavorite.setOnClickListener {
                onFavoriteClick(destination, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDestinationBinding.inflate(inflater, parent, false)
        return DestinationViewHolder(binding)
    }

    override fun getItemCount(): Int = destinations.size

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(destinations[position], position)

    }

    fun updateData(newDestinations: List<Destination>) {
        this.destinations = newDestinations
        notifyDataSetChanged()
    }
}