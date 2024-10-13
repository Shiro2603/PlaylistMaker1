package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.media.view_model.MediaViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MediaViewModel(mediaPlayerInteractor = get())
    }

    viewModel {
        SearchViewModel(
            trackInteractor = get(),
            searchHistoryInteractor = get(),
            saveTrackInteractor = get())
    }

    viewModel {
        SettingsViewModel(
            themeInteractor = get(),
            sharingInteractor = get())
    }

}