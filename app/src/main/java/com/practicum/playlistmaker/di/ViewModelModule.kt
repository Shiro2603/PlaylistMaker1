package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.media.view_model.MediaViewModel
import com.practicum.playlistmaker.ui.mediateka.view_model.EditPlayListViewModel
import com.practicum.playlistmaker.ui.mediateka.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.ui.mediateka.view_model.NewPlayListViewModel
import com.practicum.playlistmaker.ui.mediateka.view_model.PlayListViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MediaViewModel(
            mediaPlayerInteractor = get(),
            playListInteractor = get(),
            favoriteTrackInteractor = get())
    }
    viewModel {
        SearchViewModel(
            trackInteractor = get(),
            searchHistoryInteractor = get())
    }

    viewModel {
        SettingsViewModel(
            themeInteractor = get(),
            sharingInteractor = get())
    }

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel {
        PlayListViewModel(get(), get())
    }

    viewModel{
        NewPlayListViewModel(get())
    }

    viewModel {
        EditPlayListViewModel(get())
    }

}