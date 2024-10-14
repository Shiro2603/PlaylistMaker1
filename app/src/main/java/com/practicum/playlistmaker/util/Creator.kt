package com.practicum.playlistmaker.util


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.settings.impl.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.SongApi
import com.practicum.playlistmaker.data.player.MediaPlayerRepository
import com.practicum.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.SaveTrackRepository
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.data.search.SearchHistoryRepository
import com.practicum.playlistmaker.domain.settings.ThemeInteractor
import com.practicum.playlistmaker.data.settings.ThemeRepository
import com.practicum.playlistmaker.data.search.TrackRepository
import com.practicum.playlistmaker.data.search.impl.SaveTrackRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.practicum.playlistmaker.data.sharing.ResourceProvider
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.ResourceProviderImpl
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.SaveTrackInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.impl.SaveTrackInteractorImpl
import com.practicum.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.settings.impl.ThemeInteractorImpl
import com.practicum.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.sharing.ResourceProviderInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.ResourceProviderInteractorImpl
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl


object Creator {
    private fun getTracksRepository(context: Context, songApi : SongApi): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(
            context,
            songApi
        ))
    }

    fun provideTracksInteractor(context: Context, songApi : SongApi): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context, songApi))
    }

    private fun getSearchHistoryRepository(sharedPreferences: SharedPreferences, gson: Gson) : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            sharedPreferences,
            gson = gson
        )
    }

    fun provideSearchHistoryInteractor(sharedPreferences: SharedPreferences, gson: Gson): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(sharedPreferences,
            gson ))

    }

    private fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(context)
    }

    fun provideThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context))
    }

    private fun getSharingRepository(activity: Activity) : ExternalNavigatorRepository {
        return ExternalNavigatorRepositoryImpl(activity)
    }

    fun provideSharingInteractor(activity: Activity, context: Context) : SharingInteractor {
        return SharingInteractorImpl(getSharingRepository(activity), getResourceProviderRepository(context))
    }

    private fun getMediaPlayerRepository(mediaPlayer: MediaPlayer) : MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(mediaPlayer)
    }

    fun provideMediaPlayerInteractor(mediaPlayer: MediaPlayer) : MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository(mediaPlayer))
    }

    private fun getSaveTrackRepository(context: Context) : SaveTrackRepository {
        return SaveTrackRepositoryImpl(context)
    }

    fun provideSaveTrackInteractor(context: Context) : SaveTrackInteractor {
        return SaveTrackInteractorImpl(getSaveTrackRepository(context))
    }

    private fun getResourceProviderRepository(context: Context) : ResourceProvider {
        return ResourceProviderImpl(context)
    }

    fun provideResourceProviderInteractor(context: Context) : ResourceProviderInteractor {
        return ResourceProviderInteractorImpl(ResourceProviderImpl(context))
    }

}

