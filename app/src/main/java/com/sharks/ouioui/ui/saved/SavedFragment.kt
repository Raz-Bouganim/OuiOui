package com.sharks.ouioui.ui.saved

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
import com.sharks.ouioui.databinding.FragmentSavedBinding
import com.sharks.ouioui.utils.DestinationAdapter
import com.sharks.ouioui.utils.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment displaying saved favorite destinations.
 */
@AndroidEntryPoint
class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private lateinit var savedAdapter: DestinationAdapter

    private var lastToggledDestination: String? = null
    private var lastFavorites: List<Destination> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeFavorites()
    }

    /**
     * Sets up the RecyclerView and its adapter.
     * */
    private fun setupRecyclerView() {
        binding.recyclerViewSavedDestinations.layoutManager = LinearLayoutManager(requireContext())
        savedAdapter = DestinationAdapter(
            emptyList(),
            onFavoriteClick = { destination, _ ->
                lastToggledDestination = destination.title
                favoriteViewModel.removeFavorite(destination)
            },
            isFavorite = { destination ->
                favoriteViewModel.favorites.value?.any { it.title == destination.title } == true
            }
        )
        binding.recyclerViewSavedDestinations.adapter = savedAdapter
    }

    /**
     * Observes the favorites LiveData and updates the UI.
     */
    private fun observeFavorites() {
        favoriteViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            savedAdapter.updateData(favorites)
            lastToggledDestination?.let { toggledTitle ->
                val wasFavorite = lastFavorites.any { it.title == toggledTitle }
                val isFavorite = favorites.any { it.title == toggledTitle }
                if (wasFavorite != isFavorite) {
                    val message =
                        if (isFavorite) getString(R.string.addedToFavoritesText) else getString(R.string.removedFromFavoritesText)
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