package com.sharks.ouioui.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharks.ouioui.utils.EventAdapter
import com.sharks.ouioui.databinding.FragmentEventsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying events based on user search.
 */
@AndroidEntryPoint
class EventsFragment : Fragment() {
    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventsViewModel by activityViewModels()
    private lateinit var adapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = EventAdapter(emptyList())
        binding.recyclerViewSearchedEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchedEvents.adapter = adapter

        binding.eventsSearchBar.setOnClickListener {
            val city = binding.eventsSearchEditText.text.toString().trim()
            if (city.isNotEmpty()) viewModel.fetchEvents(city)
        }

        viewModel.events.observe(viewLifecycleOwner) { adapter.updateData(it) }
        viewModel.error.observe(viewLifecycleOwner) { if (it.isNotEmpty()) Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}