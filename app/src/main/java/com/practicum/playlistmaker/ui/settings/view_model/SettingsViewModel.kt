package com.practicum.playlistmaker.ui.settings.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.ThemeInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor


class SettingsViewModel(
    private val themeInteractor: ThemeInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val _isDarkThemeEnabled = MutableLiveData<Boolean>()
    val isDarkThemeEnabled: LiveData<Boolean> get() = _isDarkThemeEnabled

    init {
        _isDarkThemeEnabled.value = themeInteractor.isDarkThemeEnabled()
    }

    fun toggleTheme(isDarkTheme: Boolean) {
        themeInteractor.setDarkTheme(isDarkTheme)
        _isDarkThemeEnabled.value = isDarkTheme
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }
}