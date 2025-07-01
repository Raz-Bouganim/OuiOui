package com.sharks.ouioui.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharks.ouioui.R
import com.sharks.ouioui.data.model.Event
import com.sharks.ouioui.databinding.ItemEventBinding

class EventAdapter(private var events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    fun updateData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.textViewEventTitle.text = event.title
            binding.textViewEventStartDate.text = event.date?.start_date ?: ""
            binding.textViewEventWhen.text = event.date?.when_ ?: ""
            binding.textViewEventAddress.text = event.address?.joinToString(", ") ?: ""

            if (!event.thumbnail.isNullOrEmpty()) {
                binding.imageViewEventThumbnail.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(event.thumbnail)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(binding.imageViewEventThumbnail)
            } else {
                binding.imageViewEventThumbnail.visibility = View.GONE
            }
        }
    }
}