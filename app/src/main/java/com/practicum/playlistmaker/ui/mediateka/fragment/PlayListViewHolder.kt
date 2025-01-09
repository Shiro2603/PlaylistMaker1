package com.practicum.playlistmaker.ui.mediateka.fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlayListViewInMediatekaBinding
import com.practicum.playlistmaker.domain.mediateka.model.PlayList

class PlayListViewHolder(private val binding: PlayListViewInMediatekaBinding) : RecyclerView.ViewHolder(binding.root) {


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