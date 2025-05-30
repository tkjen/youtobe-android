package com.tkjen.youtube

import android.app.Application
import com.tkjen.youtube.utils.ThemeManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YoutubeCloneApplication:Application()
{
    lateinit var themeManager: ThemeManager
        private set
       override fun onCreate() {
           super.onCreate()
           themeManager = ThemeManager(this)

           // Áp dụng theme đã lưu khi app khởi động
           themeManager.applyTheme(themeManager.getTheme())
       }
    companion object {
        /**
         * Helper method to get ThemeManager from any Context
         */
        fun getThemeManager(context: android.content.Context): ThemeManager {
            return (context.applicationContext as YoutubeCloneApplication).themeManager
        }
    }
}