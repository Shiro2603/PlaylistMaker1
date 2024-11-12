package com.practicum.playlistmaker.ui.search.fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.SongsViewBinding
import com.practicum.playlistmaker.util.Until.dpToPx
import com.practicum.playlistmaker.domain.search.model.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SongsViewHolder(private val binding: SongsViewBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(song: Track) {
        Glide.with(itemView.context)
            .load(song.artworkUrl100)
            .placeholder(R.drawable.pc_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(binding.ivSongPicture)

        binding.tvSongName.text = song.trackName
        binding.tvGroupName.text = song.artistName
        binding.tvSongTime.text =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(song.trackTimeMillis))


    }


}

