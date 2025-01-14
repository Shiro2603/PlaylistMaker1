package com.practicum.playlistmaker.di

import android.system.Os.bind
import com.practicum.playlistmaker.domain.mediateka.PlayListInteractor
import com.practicum.playlistmaker.domain.mediateka.impl.PlayListInteractorImpl
import com.practicum.playlistmaker.domain.player.FavoriteTrackInteractor
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.FavoriteTrackInteractorImpl
import com.practicum.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
//import com.practicum.playlistmaker.domain.search.impl.SaveTrackInteractorImpl
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

   factory<FavoriteTrackInteractor> {
       FavoriteTrackInteractorImpl(get())
   }

    factory<PlayListInteractor> {
        PlayListInteractorImpl(get(), get())
    }


}