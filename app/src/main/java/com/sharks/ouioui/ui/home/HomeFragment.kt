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
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Fragment displaying featured and discoverable destinations.
 * Handles location permissions, auto-scrolling, and favorite toggling.
 */
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            fetchCountryAndDestinations()
        } else {
            homeViewModel.fetchDestinationsForCountry("France")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation()
        setupAdapters()
        observeViewModels()

        if (homeViewModel.locationDestinations.value.isNullOrEmpty()) {
            requestLocationAndFetchFeatured()
        }
        if (homeViewModel.worldDestinations.value.isNullOrEmpty()) {
            //homeViewModel.fetchWorldDestinations()
        }
    }

    /**
     * Sets up navigation for the search button.
     */
    private fun setupNavigation() {
        binding.searchButtonCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    /**
     * Initializes adapters and RecyclerViews for featured and discover destinations.
     */
    private fun setupAdapters() {
        featuredAdapter = DestinationAdapter(
            emptyList(),
            onFavoriteClick = { destination, _ ->
                lastToggledDestination = destination.title
                favoriteViewModel.toggleFavorite(destination)
            },
            isFavorite = { destination ->
                favoriteViewModel.favorites.value?.any { it.title == destination.title } == true
            }
        )
        binding.recyclerViewFeaturedDestination.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFeaturedDestination.adapter = featuredAdapter

        discoverAdapter = DestinationAdapter(
            emptyList(),
            onFavoriteClick = { destination, _ ->
                lastToggledDestination = destination.title
                favoriteViewModel.toggleFavorite(destination)
            },
            isFavorite = { destination ->
                favoriteViewModel.favorites.value?.any { it.title == destination.title } == true
            }
        )
        binding.recyclerViewDiscoverDestinations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDiscoverDestinations.adapter = discoverAdapter
    }

    /**
     * Observes LiveData from ViewModels to update UI components.
     */
    private fun observeViewModels() {
        homeViewModel.loadingWorld.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarDiscover.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewDiscoverDestinations.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        homeViewModel.loadingLocationDestinations.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarFeatured.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewFeaturedDestination.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        homeViewModel.locationDestinations.observe(viewLifecycleOwner) { destinations ->
            featuredAdapter.updateData(destinations)
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
                    val message = if (isFavorite) getString(R.string.addedToFavoritesText) else getString(R.string.removedFromFavoritesText)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
            lastFavorites = favorites
        }
    }

    /**
     * Requests location permission and fetches featured destinations based on the user's location.
     */
    private fun requestLocationAndFetchFeatured() {
        when {
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                fetchCountryAndDestinations()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    /**
     * Fetches the user's country based on their location and featured destinations.
     * If the location cannot be determined, defaults to France.
     */
    private fun fetchCountryAndDestinations() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return

        fusedLocationClient.getCurrentLocation(
            com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val country = addresses?.firstOrNull()?.countryName
                if (country != null) {
                    homeViewModel.fetchDestinationsForCountry(country)
                } else {
                    Log.e("HomeFragment", getString(R.string.unableToDetermineCountryText))
                    homeViewModel.fetchDestinationsForCountry("France")
                }
            } else {
                Log.e("HomeFragment", getString(R.string.locationIsNullText))
                homeViewModel.fetchDestinationsForCountry("France")
            }
        }.addOnFailureListener {
            Log.e("HomeFragment", getString(R.string.failedToGetLocationText), it)
            homeViewModel.fetchDestinationsForCountry("France")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
        _binding = null
    }
}