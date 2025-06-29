package com.sharks.ouioui.ui.settings

import com.sharks.ouioui.R
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.sharks.ouioui.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSettingsBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dark mode toggle
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isDark = prefs.getBoolean("dark_mode", false)
        binding.switchDark.isChecked = isDark
        binding.switchDark.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean("dark_mode", checked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (checked) AppCompatDelegate.MODE_NIGHT_YES
                else           AppCompatDelegate.MODE_NIGHT_NO
            )
            requireActivity().recreate()
        }

        // Language: show current device language
        val locale = Locale.getDefault()
        binding.tvLanguage.text = locale.displayLanguage

        // About: show a Toast
        binding.rowAbout.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.aboutOuiOuiText), Toast.LENGTH_SHORT).show()
        }

        // Rate: show a rating dialog
        binding.rowRate.setOnClickListener {
            val ratings = arrayOf("★☆☆☆☆", "★★☆☆☆", "★★★☆☆", "★★★★☆", "★★★★★")
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.rateThisAppText))
                .setItems(ratings) { dlg, idx ->
                    Toast.makeText(requireContext(), "You rated: ${ratings[idx]}", Toast.LENGTH_SHORT).show()
                    dlg.dismiss()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}