package com.practicum.playlistmaker.ui.media.fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlayListViewInMediaBinding
import com.practicum.playlistmaker.domain.mediateka.model.PlayList

class PlayListMediaViewHolder(private val binding: PlayListViewInMediaBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(playList : PlayList) {

        Glide.with(binding.playListPicture.context)
            .load(playList.urlImager)
            .placeholder(R.drawable.pc_placeholder)
            .centerCrop()
            .into(binding.playListPicture)
        binding.playListName.text = playList.playListName
        binding.playListCount.text = "${playList.tracksCount} ${playList.tracksCount?.let {
            getTrackWordForm(
                it
            )
        }}"
    }

    private fun getTrackWordForm(count: Int): String {
        val absCount = count % 100
        val lastDigit = absCount % 10
        return when {
            absCount in 11..19 -> "треков"
            lastDigit == 1 -> "трек"
            lastDigit in 2..4 -> "трека"
            else -> "треков"
        }
    }

}