package com.practicum.playlistmaker.presentation.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.Until.dpToPx
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SongsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val songPicture : ImageView = itemView.findViewById(R.id.iv_song_picture)
    private val songName : TextView = itemView.findViewById(R.id.tv_song_name)
    private val groupName : TextView = itemView.findViewById(R.id.tv_group_name)
    private val songTime : TextView = itemView.findViewById(R.id.tv_song_time)




    fun bind(song: Track) {
        Glide.with(itemView.context)
            .load(song.artworkUrl100)
            .placeholder(R.drawable.pc_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(songPicture)


        songName.text = song.trackName
        groupName.text = song.artistName
        songTime.text =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(song.trackTimeMillis))







    }


}

