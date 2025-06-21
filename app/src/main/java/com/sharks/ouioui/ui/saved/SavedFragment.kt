package com.sharks.ouioui.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharks.ouioui.databinding.FragmentSavedBinding
import com.sharks.ouioui.utils.DestinationAdapter
import com.sharks.ouioui.utils.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var savedAdapter: DestinationAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewSavedDestinations.layoutManager = LinearLayoutManager(requireContext())

        savedAdapter = DestinationAdapter(
            emptyList(),
            onFavoriteClick = { destination, position->
                favoriteViewModel.removeFavorite(destination)
                savedAdapter.notifyItemChanged(position)
                Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
            },
            isFavorite = { destination ->
                favoriteViewModel.favorites.value?.any { it.id == destination.id } ?: false
            }
        )
        binding.recyclerViewSavedDestinations.adapter = savedAdapter

        favoriteViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            savedAdapter.updateData(favorites)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}