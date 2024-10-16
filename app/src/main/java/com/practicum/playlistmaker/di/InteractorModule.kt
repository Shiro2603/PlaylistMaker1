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
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val interactorModule = module {

    factoryOf(::MediaPlayerInteractorImpl) {
        bind<MediaPlayerInteractor>()
    }

    factoryOf(::SaveTrackInteractorImpl) {
        bind<SaveTrackInteractor>()
    }

    factoryOf(::SearchHistoryInteractorImpl) {
        bind<SearchHistoryInteractor>()
    }

    factoryOf(::TracksInteractorImpl) {
        bind<TracksInteractor>()
    }

    factoryOf(::ThemeInteractorImpl) {
        bind<ThemeInteractor>()
    }

    factoryOf(::ResourceProviderInteractorImpl) {
        bind<ResourceProviderInteractor>()
    }

    factoryOf(::SharingInteractorImpl) {
        bind<SharingInteractor>()
    }


}