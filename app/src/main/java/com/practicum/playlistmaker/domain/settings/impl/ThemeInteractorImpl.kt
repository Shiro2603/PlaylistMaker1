package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.domain.settings.ThemeInteractor
import com.practicum.playlistmaker.data.settings.ThemeRepository

class ThemeInteractorImpl(private val repository : ThemeRepository) : ThemeInteractor {
    override fun isDarkThemeEnabled(): Boolean {
        return repository.isDarkThemeEnabled()
    }

    override fun setDarkTheme(enabled: Boolean) {
        repository.setDarkTheme(enabled)
    }
}