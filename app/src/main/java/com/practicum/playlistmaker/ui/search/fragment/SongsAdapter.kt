package com.practicum.playlistmaker.ui.search.fragment

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SongsViewBinding
import com.practicum.playlistmaker.domain.search.model.Track


class SongsAdapter(
    private var tracks : List<Track>,
    var onClickedTrack : ((Track) -> Unit)? = null,
) : RecyclerView.Adapter<SongsViewHolder>() {


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
        val binding = SongsViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                onClickedTrack?.invoke(tracks[position])
                updateData(tracks)
            }
        }
    }


    override fun getItemCount(): Int {
        return tracks.size
    }


    fun updateData(newSongs: List<Track>) {
         tracks = newSongs
        notifyDataSetChanged()

    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }



}