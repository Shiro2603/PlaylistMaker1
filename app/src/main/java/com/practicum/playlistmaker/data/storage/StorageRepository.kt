package com.practicum.playlistmaker.data.storage

import android.net.Uri

interface StorageRepository {

    fun saveToStorage(uri: Uri) : String
    fun getImageToStorage(image: String) : Uri

}