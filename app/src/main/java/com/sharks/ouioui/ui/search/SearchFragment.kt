package com.sharks.ouioui.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharks.ouioui.databinding.FragmentSearchBinding
import com.sharks.ouioui.utils.DestinationAdapter
import com.sharks.ouioui.utils.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private lateinit var adapter: DestinationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DestinationAdapter(
            emptyList(),
            onFavoriteClick = { destination, position ->
                favoriteViewModel.toggleFavorite(destination)
                adapter.notifyItemChanged(position)
                val isNowFavorite = favoriteViewModel.favorites.value?.any { it.id == destination.id } ?: false
                val message = if (isNowFavorite) "Removed from favorites" else "Added to favorites"
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            },
            isFavorite = { destination ->
                favoriteViewModel.favorites.value?.any { it.id == destination.id } ?: false
            }
        )
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