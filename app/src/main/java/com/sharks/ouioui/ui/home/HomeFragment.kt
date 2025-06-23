package com.sharks.ouioui.ui.home

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sharks.ouioui.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharks.ouioui.R
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.utils.DestinationAdapter
import com.sharks.ouioui.utils.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import com.google.android.gms.location.LocationServices

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    private lateinit var discoverAdapter: DestinationAdapter
    private lateinit var featuredAdapter: DestinationAdapter

    private var lastToggledDestination: String? = null
    private var lastFavorites: List<Destination> = emptyList()

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

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

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
        featuredAdapter = DestinationAdapter(
            emptyList(),
            onFavoriteClick = { destination, position ->
                lastToggledDestination = destination.title
                favoriteViewModel.toggleFavorite(destination)
            },
            isFavorite = { destination ->
                favoriteViewModel.favorites.value?.any { it.title == destination.title } == true
            }
        )
        binding.recyclerViewFeaturedDestination.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFeaturedDestination.adapter = featuredAdapter

        // Display discover destinations
        discoverAdapter = DestinationAdapter(
            emptyList(),
            onFavoriteClick = { destination, position ->
                lastToggledDestination = destination.title
                favoriteViewModel.toggleFavorite(destination)
            },
            isFavorite = { destination ->
                favoriteViewModel.favorites.value?.any { it.title == destination.title } == true
            }
        )
        binding.recyclerViewDiscoverDestinations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDiscoverDestinations.adapter = discoverAdapter

        homeViewModel.loadingWorld.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarDiscover.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewDiscoverDestinations.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        homeViewModel.loadingLocationDestinations.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarFeatured.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewFeaturedDestination.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        homeViewModel.locationDestinations.observe(viewLifecycleOwner) { destinations ->
            val shuffled = destinations
            featuredAdapter.updateData(shuffled)
            featuredPosition = 0
            autoScrollHandler.removeCallbacks(autoScrollRunnable)
            autoScrollHandler.postDelayed(autoScrollRunnable, 7000)
        }

        homeViewModel.worldDestinations.observe(viewLifecycleOwner) { destinations ->
            discoverAdapter.updateData(destinations.take(30))
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        favoriteViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            featuredAdapter.updateData(homeViewModel.locationDestinations.value ?: emptyList())
            discoverAdapter.updateData(homeViewModel.worldDestinations.value?.take(30) ?: emptyList())

            lastToggledDestination?.let { toggledTitle ->
                val wasFavorite = lastFavorites.any { it.title == toggledTitle }
                val isFavorite = favorites.any { it.title == toggledTitle }
                if (wasFavorite != isFavorite) {
                    val message = if (isFavorite) "Added to favorites" else "Removed from favorites"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
            lastFavorites = favorites
        }

        requestLocationAndFetchFeatured()
        //homeViewModel.fetchWorldDestinations()
    }

    @Suppress("DEPRECATION")
    private fun requestLocationAndFetchFeatured() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            fetchCountryAndDestinations()
        }
    }

    @Deprecated("DEPRECATION")
    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchCountryAndDestinations()
        } else {
            homeViewModel.fetchDestinationsForCountry("France")
        }
    }

    @Suppress("DEPRECATION")
    private fun fetchCountryAndDestinations() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val country = addresses?.firstOrNull()?.countryName
                if (country != null) {
                    homeViewModel.fetchDestinationsForCountry(country)
                } else {
                    Toast.makeText(requireContext(), "Unable to determine country, showing France", Toast.LENGTH_SHORT).show()
                    homeViewModel.fetchDestinationsForCountry("France")
                }
            } else {
                Toast.makeText(requireContext(), "Location is null, showing France", Toast.LENGTH_SHORT).show()
                homeViewModel.fetchDestinationsForCountry("France")
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to get location, showing France", Toast.LENGTH_SHORT).show()
            homeViewModel.fetchDestinationsForCountry("France")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
        _binding = null
    }
}