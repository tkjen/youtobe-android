package com.tkjen.youtube.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager(private val context: Context) {

    private val sharedPref = context.getSharedPreferences("theme_preferences", Context.MODE_PRIVATE)

    companion object {
        const val THEME_KEY = "selected_theme"
        const val LIGHT_MODE = "light"
        const val DARK_MODE = "dark"
        const val SYSTEM_DEFAULT = "system"
    }

    /**
     * Lưu theme được chọn
     */
    fun setTheme(theme: String) {
        sharedPref.edit().putString(THEME_KEY, theme).apply()
        applyTheme(theme)
    }

    /**
     * Lấy theme hiện tại
     */
    fun getTheme(): String {
        return sharedPref.getString(THEME_KEY, SYSTEM_DEFAULT) ?: SYSTEM_DEFAULT
    }

    /**
     * Áp dụng theme
     */
    fun applyTheme(theme: String) {
        when (theme) {
            LIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            DARK_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            SYSTEM_DEFAULT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    /**
     * Kiểm tra xem có đang ở Dark Mode không
     */
    fun isDarkMode(): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * Toggle giữa Light và Dark mode
     */
    fun toggleDarkMode() {
        val currentTheme = getTheme()
        if (currentTheme == SYSTEM_DEFAULT) {
            // Nếu đang theo system, chuyển sang dark hoặc light tùy theo trạng thái hiện tại
            if (isDarkMode()) {
                setTheme(LIGHT_MODE)
            } else {
                setTheme(DARK_MODE)
            }
        } else {
            // Toggle giữa light và dark
            val newTheme = if (currentTheme == LIGHT_MODE) DARK_MODE else LIGHT_MODE
            setTheme(newTheme)
        }
    }

    /**
     * Lấy tên hiển thị của theme
     */
    fun getThemeDisplayName(): String {
        return when (getTheme()) {
            LIGHT_MODE -> "Chế độ sáng"
            DARK_MODE -> "Chế độ tối"
            SYSTEM_DEFAULT -> "Theo hệ thống"
            else -> "Theo hệ thống"
        }
    }
}