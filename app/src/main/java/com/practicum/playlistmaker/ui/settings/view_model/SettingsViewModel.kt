package com.practicum.playlistmaker.ui.settings.view_model

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.settings.ThemeInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor


class SettingsViewModel(
    private val themeInteractor: ThemeInteractor,
    private val sharingInteractor: SharingInteractor,
    activity: Activity
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


    companion object{
        fun getViewModelFactory(
            themeInteractor: ThemeInteractor,
            sharingInteractor: SharingInteractor) : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(themeInteractor, sharingInteractor, Activity() ) as T
                }
            }
    }




}