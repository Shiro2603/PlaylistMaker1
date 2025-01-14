package com.practicum.playlistmaker.data.storage.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.data.storage.StorageRepository
import java.io.File
import java.io.FileOutputStream

class StorageRepositoryImpl(private val context: Context) : StorageRepository {

    override fun saveToStorage(uri: Uri) : String {
            val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")

            if(!filePath.exists()) {
                filePath.mkdirs()
            }

            val imageName = "${System.currentTimeMillis()}.jpg"
            val file = File(filePath, imageName)

            val inputStream = context.contentResolver.openInputStream(uri)

            val outputStream = FileOutputStream(file)

            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return imageName
    }

    override fun getImageToStorage(image: String) :  Uri  {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, image)

        return file.toUri()
    }
}