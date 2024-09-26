package com.practicum.playlistmaker.domain.api

interface ThemeInteractor {

    fun isDarkThemeEnabled(): Boolean

    fun setDarkTheme(enabled: Boolean)
}