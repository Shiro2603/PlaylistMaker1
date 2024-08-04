package com.practicum.playlistmaker.search

import SearchHistoryManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R


class SongsAdapter(
    private val songs : List<Track>
    ,private val context: Context,
) : RecyclerView.Adapter<SongsViewHolder>() {


    val searchHistoryManager = SearchHistoryManager(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.songs_view, parent, false)
        return SongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(songs[position])
        val track = songs[position]
        holder.itemView.setOnClickListener {
            searchHistoryManager.addTrackToHistory(track)

        }

    }

    override fun getItemCount(): Int {
        return songs.size
    }
}