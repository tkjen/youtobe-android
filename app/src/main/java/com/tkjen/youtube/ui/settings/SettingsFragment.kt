package com.tkjen.youtube.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tkjen.youtube.R
import com.tkjen.youtube.YoutubeCloneApplication
import com.tkjen.youtube.databinding.FragmentSettingsBinding

import com.tkjen.youtube.utils.ThemeManager

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding : FragmentSettingsBinding
    private lateinit var themeManager: ThemeManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ThemeManager
        themeManager = YoutubeCloneApplication.getThemeManager(requireContext())

        setupClickListeners()
        updateUI()
    }

    private fun setupClickListeners() {
        // Back button
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Radio group listener
        binding.radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioLightMode.id -> {
                    themeManager.setTheme(ThemeManager.LIGHT_MODE)
                    updateUI()
                }
                binding.radioDarkMode.id -> {
                    themeManager.setTheme(ThemeManager.DARK_MODE)
                    updateUI()
                }
                binding.radioSystemDefault.id -> {
                    themeManager.setTheme(ThemeManager.SYSTEM_DEFAULT)
                    updateUI()
                }
            }
        }

        // Switch listener
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                themeManager.setTheme(ThemeManager.DARK_MODE)
            } else {
                themeManager.setTheme(ThemeManager.LIGHT_MODE)
            }
            updateUI()
        }
    }

    private fun updateUI() {
        val currentTheme = themeManager.getTheme()

        // Cập nhật RadioGroup
        when (currentTheme) {
            ThemeManager.LIGHT_MODE -> {
                binding.radioLightMode.isChecked = true
                binding.switchDarkMode.isChecked = false
            }
            ThemeManager.DARK_MODE -> {
                binding.radioDarkMode.isChecked = true
                binding.switchDarkMode.isChecked = true
            }
            ThemeManager.SYSTEM_DEFAULT -> {
                binding.radioSystemDefault.isChecked = true
                binding.switchDarkMode.isChecked = themeManager.isDarkMode()
            }
        }

        // Cập nhật text hiển thị theme hiện tại
        binding.tvCurrentTheme.text = themeManager.getThemeDisplayName()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

}