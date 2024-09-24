package com.practicum.playlistmaker.data

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

const val THEME_PREFERENCES = "theme"
const val THEME_KEY = "theme_key"

class SharedPreferencesTheme : Application() {

     var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(THEME_KEY, false)

        setAppTheme(darkTheme)


    }


    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        val sharedPrefs = getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putBoolean(THEME_KEY, darkThemeEnabled)
            apply()
        }

        setAppTheme(darkThemeEnabled)


    }

    private fun setAppTheme(darkThemeEnabled : Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }


}