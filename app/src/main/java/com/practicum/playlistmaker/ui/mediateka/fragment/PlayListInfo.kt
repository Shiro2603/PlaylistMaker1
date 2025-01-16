package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val playList = args.playList

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is PlayListState.SinglePlaylist -> {
                    displayPlaylistInfo(it.playList)
                    displayPlayListMoreMenu(it.playList)
                }

                else -> {}
            }
        }

        viewModel.getPlayListById(playList.id!!)
        viewModel.loadPlayListTrack(playList.tracksIds)


        tracksAdapter = SongsAdapter(emptyList())
        binding.rvTrackPlayList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTrackPlayList.adapter = tracksAdapter

        viewModel.stateTrackLiveData.observe(viewLifecycleOwner) { tracks ->
            tracksAdapter?.updateData(tracks)
            val totalDuration = viewModel.calculateTotalDuration(tracks)
            binding.playListTotalTime.text = "${totalDuration} минут"
            emptyTrackList(tracks)
        }

        tracksAdapter?.onClickedTrack = {track ->
            val action = PlayListInfoDirections.actionPlayListInfoToMediaFragment(track)
            findNavController().navigate(action)
        }


        tracksAdapter?.onLongClickedTrack = {

            confirmDialog1 = MaterialAlertDialogBuilder(requireContext(), R.style.alertThemePlayList)
                .setTitle("Удалить трек")
                .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
                .setNegativeButton("Нет") {dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Да") {dialog, which ->
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
            .setTitle("Удалить плейлист")
            .setMessage("Хотите удалить плейлист?")
            .setNegativeButton("Нет") {dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Да") {dialog, which ->
                viewModel.deletePlayList(playList)
                findNavController().navigateUp()
            }


        binding.moreMenuDeletePlayList.setOnClickListener {
            confirmDialog2.show()
        }

    }

    private fun displayPlaylistInfo(playList: PlayList) {
        Glide.with(binding.imagePlayList.context)
            .load(playList.urlImager)
            .placeholder(R.drawable.pc_placeholder)
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


}