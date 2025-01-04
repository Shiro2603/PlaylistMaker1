package com.practicum.playlistmaker.ui.mediateka.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlayListViewBinding
import com.practicum.playlistmaker.domain.mediateka.model.PlayList

class PlayListAdapter(
    private var playList: List<PlayList>,
    var onClickedTrack : ((PlayList) -> Unit)? = null,
) : RecyclerView.Adapter<PlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val binding = PlayListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playList[position])
        holder.itemView.setOnClickListener {
            onClickedTrack?.invoke(playList[position])
            updateData(playList)
        }
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    fun updateData(playListNew: List<PlayList>) {
        playList = playListNew
        notifyDataSetChanged()

    }


}