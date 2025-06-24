package com.sharks.ouioui.ui.settings

import com.sharks.ouioui.R
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
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


        // Email placeholder
        binding.tvEmail.text = getString(R.string.userExampleEmailText)

        // Logout button
        binding.btnLogout.setOnClickListener {
            // TODO: implement logout
            android.widget.Toast
                .makeText(requireContext(), getString(R.string.loggedOutText), Toast.LENGTH_SHORT)
                .show()
        }

        // About dialog
        binding.rowAbout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.aboutOuiOuiText))
                .setMessage(getString(R.string.ouiouiVersionText))
                .setPositiveButton(getString(R.string.okText), null)
                .show()
        }

        // Rate this app
        binding.rowRate.setOnClickListener {
            startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${requireContext().packageName}")
            ))
        }

        // Language selector
        binding.rowLanguage.setOnClickListener {
            val langs = arrayOf("English", "עברית")
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.selectLanguageText))
                .setItems(langs) { dlg, idx ->
                    binding.tvLanguage.text = langs[idx]
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
