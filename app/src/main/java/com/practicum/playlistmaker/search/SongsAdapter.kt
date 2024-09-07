package com.practicum.playlistmaker.search

import SearchHistoryManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.MediaActivity
import com.practicum.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SongsAdapter(
    private var songs : List<Track>
    ,private val context: Context,
) : RecyclerView.Adapter<SongsViewHolder>() {


    val searchHistoryManager = SearchHistoryManager(context)
    val displayIntent = Intent(context, MediaActivity::class.java)

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val VALUE_KEY = "SearchText"
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.songs_view, parent, false)
        return SongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(songs[position])
        val track = songs[position]
        holder.itemView.setOnClickListener {
            searchHistoryManager.addTrackToHistory(track)
            val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("TRACK_NAME", track.trackName)
            editor.putString("ARTIST_NAME", track.artistName)
            editor.putString("TRACK_TIME", SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(track.trackTime)))
            editor.putString("TRACK_PICTURE", track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            editor.putString("TRACK_COLLECTION", track.collectionName)
            editor.putString("TRACK_RELEASE_DATE", SimpleDateFormat("yyyy", Locale.getDefault()).format(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)!!
            ))
            editor.putString("TRACK_GENRE", track.primaryGenreName)
            editor.putString("TRACK_COUNTRY", track.country)
            editor.putString("TRACK_PREVIEW", track.previewUrl)
            editor.apply()


            displayIntent.putExtra("TRACK_NAME", track.trackName)
            displayIntent.putExtra("ARTIST_NAME", track.artistName)
            displayIntent.putExtra("TRACK_TIME", SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(track.trackTime)))
            displayIntent.putExtra("TRACK_PICTURE", track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            displayIntent.putExtra("TRACK_COLLECTION", track.collectionName)
            displayIntent.putExtra("TRACK_RELEASE_DATE", SimpleDateFormat("yyyy", Locale.getDefault()).format(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)!!
            ))
            displayIntent.putExtra("TRACK_GENRE", track.primaryGenreName)
            displayIntent.putExtra("TRACK_COUNTRY", track.country)



            context.startActivity(displayIntent)







        }

    }

    override fun getItemCount(): Int {
        return songs.size
    }


    fun updateData(newSongs: List<Track>) {
         songs = newSongs
        notifyDataSetChanged()
    }


}