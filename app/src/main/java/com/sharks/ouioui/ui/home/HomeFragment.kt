package com.sharks.ouioui.ui.home

import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sharks.ouioui.databinding.FragmentHomeBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharks.ouioui.R
import com.sharks.ouioui.utils.DestinationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var discoverAdapter: DestinationAdapter
    private lateinit var featuredAdapter: DestinationAdapter

    private val autoScrollHandler = Handler(Looper.getMainLooper())
    private var featuredPosition = 0

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val itemCount = featuredAdapter.itemCount
            if (itemCount > 0) {
                featuredPosition = (featuredPosition + 1) % itemCount
                binding.recyclerViewFeaturedDestination.smoothScrollToPosition(featuredPosition)
                autoScrollHandler.postDelayed(this, 7000)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButtonCard.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        // Display featured destinations in France
        featuredAdapter = DestinationAdapter(emptyList())
        binding.recyclerViewFeaturedDestination.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFeaturedDestination.adapter = featuredAdapter

        // Display discover destinations
        discoverAdapter = DestinationAdapter(emptyList())
        binding.recyclerViewDiscoverDestinations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDiscoverDestinations.adapter = discoverAdapter

        viewModel.franceDestinations.observe(viewLifecycleOwner) { destinations ->
            val shuffled = destinations.shuffled()
            featuredAdapter.updateData(shuffled)
            featuredPosition = 0
            autoScrollHandler.removeCallbacks(autoScrollRunnable)
            autoScrollHandler.postDelayed(autoScrollRunnable, 7000)
        }

        viewModel.worldDestinations.observe(viewLifecycleOwner) { destinations ->
            discoverAdapter.updateData(destinations.shuffled().take(30))
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchFranceDestinations()
        viewModel.fetchWorldDestinations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
        _binding = null
    }
}