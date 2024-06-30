package com.practicum.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SongsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    private val songPicture : ImageView = itemView.findViewById(R.id.iv_song_picture)
    private val songName : TextView = itemView.findViewById(R.id.tv_song_name)
    private val groupName : TextView = itemView.findViewById(R.id.tv_group_name)
    private val songTime : TextView = itemView.findViewById(R.id.tv_song_time)

    fun bind(song: Track) {
        Glide.with(itemView.context)
            .load(song.artworkUrl100)
            .placeholder(R.drawable.ic_close)
            .transform(RoundedCorners(2))
            .centerCrop()
            .into(songPicture)


        songName.text = song.trackName
        groupName.text = song.artisName
        songTime.text = song.trackTime
    }
}