package com.practicum.playlistmaker.data.settings

interface ThemeRepository {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkTheme(enabled: Boolean)
}