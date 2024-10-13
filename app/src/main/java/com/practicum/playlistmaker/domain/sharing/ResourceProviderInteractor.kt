package com.practicum.playlistmaker.domain.sharing

interface ResourceProviderInteractor {
    fun getString(resId: Int): String
}