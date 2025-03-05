package com.practicum.playlistmaker.data.settings.impl

import android.content.Context
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.data.settings.ThemeRepository

class ThemeRepositoryImpl(private val context : Context) : ThemeRepository {

        private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

        override fun isDarkThemeEnabled(): Boolean {
            return sharedPreferences.getBoolean("dark_theme", false)
        }

        override fun setDarkTheme(enabled: Boolean) {
            val app = context.applicationContext as App
            app.switchTheme(enabled)
            sharedPreferences.edit().putBoolean("dark_theme", enabled).apply()
        }

}