package com.practicum.playlistmaker.data.sharing

interface ResourceProvider {
    fun getString(resId: Int): String
}