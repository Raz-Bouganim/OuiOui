package com.sharks.ouioui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sharks.ouioui.databinding.ActivityMainBinding
import com.sharks.ouioui.databinding.FragmentHomeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navHome.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }

        binding.navSearch.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

        binding.navSettings.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> highlightSelected(binding.navHome)
                R.id.searchFragment -> highlightSelected(binding.navSearch)
                R.id.savedFragment -> highlightSelected(binding.navSaved)
                R.id.settingsFragment -> highlightSelected(binding.navSettings)
            }
        }
    }

    private fun highlightSelected(selectedView: View) {
        val navItems = listOf(binding.navHome, binding.navSearch, binding.navSaved, binding.navSettings)
        navItems.forEach { item ->
            val isSelected = item == selectedView

            val icon = when (item) {
                binding.navHome -> binding.navHomeIcon
                binding.navSearch -> binding.navSearchIcon
                binding.navSaved -> binding.navSavedIcon
                binding.navSettings -> binding.navSettingsIcon
                else -> null
            }
            val text = when (item) {
                binding.navHome -> binding.navHomeText
                binding.navSearch -> binding.navSearchText
                binding.navSaved -> binding.navSavedText
                binding.navSettings -> binding.navSettingsText
                else -> null
            }

            item.isSelected = isSelected
            icon?.isSelected = isSelected
            text?.isSelected = isSelected
        }
    }
}