package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.data.sharing.ResourceProvider
import com.practicum.playlistmaker.domain.sharing.ResourceProviderInteractor

class ResourceProviderInteractorImpl(private val repository : ResourceProvider) : ResourceProviderInteractor {
    override fun getString(resId: Int): String {
        return repository.getString(resId)
    }
}