package com.practicum.playlistmaker.ui.search.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SongsViewBinding
import com.practicum.playlistmaker.domain.search.model.Track


class SongsAdapter(
    private var tracks : List<Track>,
    var onClickedTrack : ((Track) -> Unit)? = null,
    var onLongClickedTrack: ((Track) -> Unit)? = null
) : RecyclerView.Adapter<SongsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val binding = SongsViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onClickedTrack?.invoke(tracks[position])
            updateData(tracks)
        }
        holder.itemView.setOnLongClickListener {
            onLongClickedTrack?.invoke(tracks[position])
            updateData(tracks)
            true
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateData(newSongs: List<Track>) {
         tracks = newSongs
        notifyDataSetChanged()

    }


}