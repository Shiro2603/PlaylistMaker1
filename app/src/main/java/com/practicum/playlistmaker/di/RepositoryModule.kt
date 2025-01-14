package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.converters.PlayListConvertor
import com.practicum.playlistmaker.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.data.mediateka.PlayListRepository
import com.practicum.playlistmaker.data.mediateka.impl.PlayListRepositoryImpl
import com.practicum.playlistmaker.data.player.FavoriteTracksRepository
import com.practicum.playlistmaker.data.player.MediaPlayerRepository
import com.practicum.playlistmaker.data.player.impl.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.SearchHistoryRepository
import com.practicum.playlistmaker.data.search.TrackRepository
import com.practicum.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.data.settings.ThemeRepository
import com.practicum.playlistmaker.data.settings.impl.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.practicum.playlistmaker.data.sharing.ResourceProvider
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.ResourceProviderImpl
import com.practicum.playlistmaker.data.storage.StorageRepository
import com.practicum.playlistmaker.data.storage.impl.StorageRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val repositoryModule = module {

    factoryOf(::MediaPlayerRepositoryImpl) {
        bind<MediaPlayerRepository>()
    }

    factoryOf(::ThemeRepositoryImpl) {
        bind<ThemeRepository>()
    }

    factoryOf(::ExternalNavigatorRepositoryImpl) {
        bind<ExternalNavigatorRepository>()
    }

    factoryOf(::SearchHistoryRepositoryImpl) {
        bind<SearchHistoryRepository>()
    }

    factoryOf(::ResourceProviderImpl) {
        bind<ResourceProvider>()
    }

    factoryOf(::TrackRepositoryImpl) {
        bind<TrackRepository>()
    }

    factory { TrackDbConvertor() }

    factory { PlayListConvertor(get()) }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

   factory<PlayListRepository> {
       PlayListRepositoryImpl(get(), get())
   }

    factory<StorageRepository> {
        StorageRepositoryImpl(get())
    }

}