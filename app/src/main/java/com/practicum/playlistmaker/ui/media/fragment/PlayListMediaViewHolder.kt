package com.practicum.playlistmaker.ui.media.fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlayListViewInMediaBinding
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.util.Until.dpToPx

class PlayListMediaViewHolder(private val binding: PlayListViewInMediaBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(playList : PlayList) {

        Glide.with(binding.playListPicture.context)
            .load(playList.urlImager)
            .placeholder(R.drawable.pc_placeholder)
            .centerCrop()
            .into(binding.playListPicture)
        binding.playListName.text = playList.playListName
        binding.playListCount.text = playList.tracksCount.toString()
    }



}