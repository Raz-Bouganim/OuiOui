package com.sharks.ouioui.ui.search

import com.sharks.ouioui.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.databinding.FragmentSearchBinding
import com.sharks.ouioui.utils.DestinationAdapter
import com.sharks.ouioui.utils.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for searching and displaying travel destinations.
 * Handles search input, result display, and favorite toggling.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    private lateinit var adapter: DestinationAdapter

    private var lastToggledDestination: String? = null
    private var lastFavorites: List<Destination> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchBar()
        observeViewModels()
    }

    /**
     * Sets up the RecyclerView and its adapter.
     */
    private fun setupRecyclerView() {
        adapter = DestinationAdapter(
            emptyList(),
            onFavoriteClick = { destination, _ ->
                lastToggledDestination = destination.title
                favoriteViewModel.toggleFavorite(destination)
                searchViewModel.toggleFavorite(destination)
            },
            isFavorite = { destination ->
                favoriteViewModel.favorites.value?.any { it.title == destination.title } == true
            }
        )
        binding.recyclerViewSearchedDestination.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchedDestination.adapter = adapter
    }

    /**
     * Sets up the search bar and its click listener.
     */
    private fun setupSearchBar() {
        binding.searchEditText.setText(searchViewModel.lastQueryValue ?: "")
        binding.searchBar.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchViewModel.fetchDestinations(query)
            }
        }
    }

    /** Observes LiveData from ViewModels to update UI reactively. */
    private fun observeViewModels() {
        searchViewModel.destinations.observe(viewLifecycleOwner) { destinations ->
            adapter.updateData(destinations)
        }

        searchViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        favoriteViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            lastToggledDestination?.let { toggledTitle ->
                val wasFavorite = lastFavorites.any { it.title == toggledTitle }
                val isFavorite = favorites.any { it.title == toggledTitle }
                if (wasFavorite != isFavorite) {
                    val message = if (isFavorite) getString(R.string.addedToFavoritesText) else getString(R.string.removedFromFavoritesText)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
            lastFavorites = favorites
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}