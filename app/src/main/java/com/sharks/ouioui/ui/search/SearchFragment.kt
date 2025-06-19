package com.sharks.ouioui.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharks.ouioui.databinding.FragmentSearchBinding
import com.sharks.ouioui.utils.DestinationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var adapter: DestinationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DestinationAdapter(emptyList())
        binding.recyclerViewSearchedDestination.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchedDestination.adapter = adapter

        binding.searchEditText.setText(searchViewModel.lastQueryValue ?: "")

        binding.searchBar.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchViewModel.fetchDestinations(query)
            }
        }

        searchViewModel.destinations.observe(viewLifecycleOwner) { destinations ->
            adapter.updateData(destinations)
        }

        searchViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}