package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.SaveTrackInteractor
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.impl.SaveTrackInteractorImpl
import com.practicum.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.settings.ThemeInteractor
import com.practicum.playlistmaker.domain.settings.impl.ThemeInteractorImpl
import com.practicum.playlistmaker.domain.sharing.ResourceProviderInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.ResourceProviderInteractorImpl
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(repository = get())
    }

    factory<SaveTrackInteractor> {
        SaveTrackInteractorImpl(repository = get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(repository = get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(repository = get())
    }

    factory<ThemeInteractor> {
        ThemeInteractorImpl(repository = get())
    }

    factory<ResourceProviderInteractor> {
        ResourceProviderInteractorImpl(repository = get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(
            externalNavigator = get(),
            resourceProvider = get()
        )
    }


}