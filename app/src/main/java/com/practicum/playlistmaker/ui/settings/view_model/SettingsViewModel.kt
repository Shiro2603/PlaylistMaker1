package com.practicum.playlistmaker.ui.settings.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.ThemeInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class SettingsViewModel(
    private val themeInteractor: ThemeInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val _isDarkThemeEnabled =  MutableStateFlow(false)
    val isDarkThemeEnabled: StateFlow<Boolean>  = _isDarkThemeEnabled.asStateFlow()

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