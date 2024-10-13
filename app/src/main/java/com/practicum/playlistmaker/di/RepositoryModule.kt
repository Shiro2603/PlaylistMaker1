package com.practicum.playlistmaker.di

import android.app.Activity
import com.practicum.playlistmaker.data.player.MediaPlayerRepository
import com.practicum.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.SaveTrackRepository
import com.practicum.playlistmaker.data.search.SearchHistoryRepository
import com.practicum.playlistmaker.data.search.TrackRepository
import com.practicum.playlistmaker.data.search.impl.SaveTrackRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.data.settings.ThemeRepository
import com.practicum.playlistmaker.data.settings.impl.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.practicum.playlistmaker.data.sharing.ResourceProvider
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.ResourceProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory<SaveTrackRepository> {
        SaveTrackRepositoryImpl( context = androidContext())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(mediaPlayer = get())
    }

    factory<ThemeRepository> {
        ThemeRepositoryImpl(context = androidContext())
    }

    factory<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(activity = get())
    }

    factory { (activity: Activity) -> ExternalNavigatorRepositoryImpl(activity) }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            sharedPreferences = get(),
            gson = get())
    }

    factory<ResourceProvider> {
        ResourceProviderImpl(context = androidContext())
    }

    factory<TrackRepository> {
        TrackRepositoryImpl(networkClient = get())
    }

}