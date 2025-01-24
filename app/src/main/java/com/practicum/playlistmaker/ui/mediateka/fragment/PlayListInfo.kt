package com.practicum.playlistmaker.ui.mediateka.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayListInfoBinding
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.mediateka.PlayListState
import com.practicum.playlistmaker.ui.mediateka.view_model.PlayListViewModel
import com.practicum.playlistmaker.ui.search.fragment.SongsAdapter
import com.practicum.playlistmaker.util.Until.dpToPx
import com.practicum.playlistmaker.util.getTrackWordForm
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListInfo : Fragment() {

    private var _binding : FragmentPlayListInfoBinding? = null
    private val binding : FragmentPlayListInfoBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    private val viewModel by viewModel<PlayListViewModel>()
    private val args: PlayListInfoArgs by navArgs()
    private var tracksAdapter: SongsAdapter? = null
    lateinit var confirmDialog1: MaterialAlertDialogBuilder
    lateinit var confirmDialog2: MaterialAlertDialogBuilder
    private var track = ArrayList<Track>()
    private lateinit var playList: PlayList


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        playList = args.playList

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is PlayListState.PlaylistDeleted -> {
                    Toast.makeText(requireContext(), "Плейлист ${playList.playListName} успешно удален", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                is PlayListState.SinglePlaylist -> {
                    playList = it.playList
                    displayPlaylistInfo(it.playList)
                    displayPlayListMoreMenu(it.playList)
                    viewModel.loadPlayListTrack(it.playList.tracksIds)
                }

                else -> {}
            }
        }

        viewModel.getPlayListById(playList.id!!)

        tracksAdapter = SongsAdapter(track)
        binding.rvTrackPlayList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTrackPlayList.adapter = tracksAdapter

        viewModel.stateTrackLiveData.observe(viewLifecycleOwner) { tracks ->
            track.clear()
            track.addAll(tracks)
            tracksAdapter?.updateData(tracks)
            val totalDuration = viewModel.calculateTotalDuration(tracks)
            binding.playListTotalTime.text = "${totalDuration} минут"
            emptyTrackList(tracks)
        }

        viewModel.loadPlayListTrack(playList.tracksIds)

        tracksAdapter?.onClickedTrack = {track ->
            val action = PlayListInfoDirections.actionPlayListInfoToMediaFragment(track)
            findNavController().navigate(action)
        }

        tracksAdapter?.onLongClickedTrack = {

            confirmDialog1 = MaterialAlertDialogBuilder(requireContext(), R.style.alertThemePlayList)
                .setTitle(R.string.deleteTrackFromPlayList)
                .setMessage(R.string.doYouWontDeleteTrack)
                .setNegativeButton(R.string.cancel) {dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.delete) {dialog, which ->
                    viewModel.deleteTrack(it.trackId!!, playList)
                }

            confirmDialog1.show()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.moreMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.overlay.setOnClickListener {  }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when(newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> binding.overlay.isVisible = true
                }

            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        confirmDialog2 = MaterialAlertDialogBuilder(requireContext(), R.style.alertThemePlayList)
            .setTitle(R.string.deletePlayList)
            .setMessage(R.string.doYouWontDeletePlayList)
            .setNegativeButton(R.string.cancel) {dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.delete) {dialog, which ->
                viewModel.deletePlayList(playList)
            }


        binding.moreMenuDeletePlayList.setOnClickListener {
            confirmDialog2.show()
        }

        binding.icShare.setOnClickListener {
            if(track.isEmpty()) {
                Toast.makeText(requireContext(),"У вас нету треков, которыми можно поделиться", Toast.LENGTH_LONG).show()
            } else {
                sharePlayList(requireContext(), playList, track)

            }
        }

        binding.moreMenuShare.setOnClickListener {
            if(track.isEmpty()) {
                Toast.makeText(requireContext(),"У вас нету треков, которыми можно поделиться", Toast.LENGTH_LONG).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                sharePlayList(requireContext(), playList, track)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        binding.moreMenuEdit.setOnClickListener {
            val action = PlayListInfoDirections.actionPlayListInfoToEditPlayListFragment(playList)
            findNavController().navigate(action)
        }

    }

    private fun displayPlaylistInfo(playList: PlayList) {
        Glide.with(binding.imagePlayList.context)
            .load(playList.urlImager)
            .placeholder(R.drawable.pc_placeholder_playlist)
            .centerCrop()
            .into(binding.imagePlayList)
        binding.playListName.text = playList.playListName
        binding.playListDescription.text = playList.playListDescription
        binding.playListTrackCount.text = "${playList.tracksCount} ${playList.tracksCount?.let { getTrackWordForm(it) }}"

    }

    private fun displayPlayListMoreMenu(playList: PlayList) {
        Glide.with(binding.moreMenuImage.context)
            .load(playList.urlImager)
            .placeholder(R.drawable.pc_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(dpToPx(2f, requireContext())) )
            .into(binding.moreMenuImage)
        binding.moreMenuPlayListName.text = playList.playListName
        binding.moreMenuTrackCount.text = "${playList.tracksCount} ${playList.tracksCount?.let { getTrackWordForm(it) }}"
    }

    private fun emptyTrackList(track: List<Track>) {
        if(track.isEmpty()) {
            binding.emptyTracks.isVisible = true
            binding.rvTrackPlayList.isVisible = false
        } else {
            binding.emptyTracks.isVisible = false
            binding.rvTrackPlayList.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sharePlayList(context: Context, playList: PlayList, track: List<Track>) {
        viewModel.sharingPlayList(context, playList, track)
    }

}