package com.sharks.ouioui.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sharks.ouioui.databinding.FragmentHomeBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharks.ouioui.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var destinationAdapter: DestinationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButtonCard.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        destinationAdapter = DestinationAdapter(emptyList())
        binding.recyclerViewDiscoverDestinations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDiscoverDestinations.adapter = destinationAdapter
        viewModel.destinations.observe(viewLifecycleOwner) { destinations ->
            Log.d("HomeFragment", "Destinations: $destinations")
            destinationAdapter.updateData(destinations)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchDestinations("France")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}