package com.practicum.playlistmaker.ui.media.fragment

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.media.MediaPlayerState
import com.practicum.playlistmaker.ui.media.view_model.MediaViewModel
import com.practicum.playlistmaker.ui.mediateka.PlayListState
import com.practicum.playlistmaker.util.MusicService
import com.practicum.playlistmaker.util.Until.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized!" }
    private val viewModel by viewModel<MediaViewModel>()
    private var playListAdapter: PlayListMediaAdapter? = null
    private val playList = ArrayList<PlayList>()
    private val args: MediaFragmentArgs by navArgs()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
           val binder = service as MusicService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
            Log.i("MusicService", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
            Log.e("MusicService", "Service connected")
        }

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Toast.makeText(requireContext(), "Can't show notification!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val track = args.track

        requestPermissionLauncher.launch(POST_NOTIFICATIONS)

        track?.let {
            it.trackId?.let { id -> viewModel.checkFavorite(id) }
        }

        binding.btnArrayBackMedia.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.mediaLayout.visibility = if (track?.trackName == null) View.GONE else View.VISIBLE

        track?.let {
            setupTrackDetails(it)
        }


        viewModel.mediaPlayerState.observe(viewLifecycleOwner) {
            Log.d("MediaFragment", "New player state: $it")
            updateButtonAndProgress(it)
        }

        binding.buttonPlay.playbackListener = {
            viewModel.onPlayerButtonClicked()
        }

        binding.btnLikeSong.setOnClickListener {
            track?.let { viewModel.onFavoriteClicked(it) }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) {
            binding.btnLikeSong.setImageResource(
                if (it == true) R.drawable.ic_button_liked else R.drawable.ic_button_not_like
            )
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.btnAddToPlayList.setOnClickListener {
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

        playListAdapter = PlayListMediaAdapter(playList)
        binding.rvPlayListInMedia.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlayListInMedia.adapter = playListAdapter

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is PlayListState.Content -> {
                    playList.clear()
                    playList.addAll(it.playList)
                    playListAdapter?.notifyDataSetChanged()
                    playListAdapter?.updateData(it.playList)
                } else -> {}
            }

        }

        viewModel.getPlayList()

        binding.btnCreatePlayList.setOnClickListener {
            findNavController().navigate(R.id.newPlayListFragment)
        }

        playListAdapter?.onClickedTrack = {
            viewModel.addTrackToPlaylist(track, it)
        }

        viewModel.stateAddTrack.observe(viewLifecycleOwner) {
            when(it) {
                true -> {
                    val playListName = playList.find { playlist -> playlist.tracksIds.contains(track?.trackId) }?.playListName
                    playListName?.let {
                        Toast.makeText(requireContext(),
                            getString(R.string.addTrackToPlayList, it),
                            Toast.LENGTH_SHORT).show()
                    }
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    playListAdapter?.notifyDataSetChanged()
                }
                false -> {
                    val playListName = playList.find { playlist -> playlist.tracksIds.contains(track?.trackId) }?.playListName
                    playListName?.let {
                        Toast.makeText(requireContext()
                            , getString(R.string.trackAlreadyAdd, it),
                            Toast.LENGTH_SHORT).show()
                    }
                }

                else -> {}
            }
        }

    }

    override fun onPause() {
        super.onPause()

        viewModel.startForegroundPlaying()
    }

    override fun onResume() {
        super.onResume()
        viewModel.stopForegroundPlaying()
    }

    override fun onStop() {
        viewModel.startForegroundPlaying()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        unBindMusicService()

    }

    private fun setupTrackDetails(track: Track) {
        binding.trackName.text = track.trackName
        binding.trackGroup.text = track.artistName
        binding.durationSong.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(track.trackTimeMillis))

        val artworkUrl512 = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: ""
        Glide.with(binding.trackPicture)
            .load(artworkUrl512)
            .placeholder(R.drawable.ic_placeholder_media)
            .transform(RoundedCorners(dpToPx(8f, requireContext())))
            .into(binding.trackPicture)

        binding.albumSong.text = track.collectionName

        binding.yearSong.text = track.releaseDate?.let {
            SimpleDateFormat("yyyy", Locale.getDefault()).format(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)!!
            )
        } ?: "Unknown"

        binding.genreSong.text = track.primaryGenreName
        binding.countrySong.text = track.country
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            putExtra("song_url", args.track.previewUrl)
            putExtra("track_name", args.track.trackName)
            putExtra("artist_name", args.track.artistName)

        }
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }


    private fun unBindMusicService() {
        requireContext().unbindService(serviceConnection)
    }


    private fun updateButtonAndProgress(state: MediaPlayerState) {
        binding.buttonPlay.setPlaybackState(state.isPlaying)
        binding.trackTime.text = state.progress
    }

}