package com.sharks.ouioui.ui.settings

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.sharks.ouioui.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val b get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSettingsBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dark mode toggle
        b.switchDark.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        b.switchDark.setOnCheckedChangeListener { _, checked ->
            AppCompatDelegate.setDefaultNightMode(
                if (checked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Email placeholder
        b.tvEmail.text = "user@example.com"

        // Logout button
        b.btnLogout.setOnClickListener {
            // TODO: implement logout
            android.widget.Toast
                .makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT)
                .show()
        }

        // About dialog
        b.rowAbout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("About OuiOui")
                .setMessage("OuiOui v1.0\n© 2025 Your Company")
                .setPositiveButton("OK", null)
                .show()
        }

        // Rate this app
        b.rowRate.setOnClickListener {
            startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${requireContext().packageName}")
            ))
        }

        // Language selector
        b.rowLanguage.setOnClickListener {
            val langs = arrayOf("English", "עברית")
            AlertDialog.Builder(requireContext())
                .setTitle("Select Language")
                .setItems(langs) { dlg, idx ->
                    b.tvLanguage.text = langs[idx]
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
