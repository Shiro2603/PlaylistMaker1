package com.practicum.playlistmaker.ui.media.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlayListViewInMediaBinding
import com.practicum.playlistmaker.domain.mediateka.model.PlayList

class PlayListMediaAdapter(
    private var playList: List<PlayList>,
    var onClickedTrack : ((PlayList) -> Unit)? = null,
) : RecyclerView.Adapter<PlayListMediaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListMediaViewHolder {
        val binding = PlayListViewInMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayListMediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListMediaViewHolder, position: Int) {
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