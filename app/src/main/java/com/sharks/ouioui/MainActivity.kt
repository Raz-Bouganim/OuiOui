package com.sharks.ouioui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.sharks.ouioui.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity hosting the navigation and bottom navigation bar.
 * Handles theme selection and navigation highlighting.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppThemeFromPreferences()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    /**
     * Sets the app theme (dark/light) based on shared preferences.
     */
    private fun setAppThemeFromPreferences() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isDark = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    /**
     * Sets up bottom navigation and current page highlighting.
     */
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navHome.setOnClickListener { navController.navigate(R.id.homeFragment) }
        binding.navSearch.setOnClickListener { navController.navigate(R.id.searchFragment) }
        binding.navSaved.setOnClickListener { navController.navigate(R.id.savedFragment) }
        binding.navSettings.setOnClickListener { navController.navigate(R.id.settingsFragment) }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> highlightSelected(binding.navHome)
                R.id.searchFragment -> highlightSelected(binding.navSearch)
                R.id.savedFragment -> highlightSelected(binding.navSaved)
                R.id.settingsFragment -> highlightSelected(binding.navSettings)
            }
        }
    }

    /**
     * Highlights the selected navigation item and its icon/text.
     */
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