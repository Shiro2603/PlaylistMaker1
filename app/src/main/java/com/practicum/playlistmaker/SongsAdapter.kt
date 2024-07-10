package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SongsAdapter(
    private val songs : List<Track>
) : RecyclerView.Adapter<SongsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.songs_view, parent, false)
        return SongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}