package com.practicum.playlistmaker.domain.settings

interface ThemeInteractor {

    fun isDarkThemeEnabled(): Boolean

    fun setDarkTheme(enabled: Boolean)
}