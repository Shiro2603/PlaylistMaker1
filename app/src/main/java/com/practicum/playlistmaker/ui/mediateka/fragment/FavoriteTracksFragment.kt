package com.practicum.playlistmaker.ui.mediateka.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.media.activity.MediaActivity
import com.practicum.playlistmaker.ui.mediateka.FavoriteState
import com.practicum.playlistmaker.ui.mediateka.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.ui.search.fragment.SongsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private var _binding : FragmentFavoriteTracksBinding? = null
    private val binding : FragmentFavoriteTracksBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    private val viewModel by viewModel<FavoriteTracksViewModel>()
    private var favoriteAdapter: SongsAdapter? = null
    private val track = ArrayList<Track>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter = SongsAdapter(track)
        binding.rvFavoriteList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.rvFavoriteList.adapter = favoriteAdapter

        favoriteAdapter?.onClickedTrack = {track ->
            val intent = Intent(requireContext(), MediaActivity::class.java)
            intent.putExtra(SAVE_TRACK, track)
            requireActivity().startActivity(intent)
        }

        viewModel.stateLiveDate.observe(viewLifecycleOwner) {
            when(it) {
                is FavoriteState.Content -> {
                    binding.rvFavoriteList.visibility = View.VISIBLE
                    binding.placeHolderFavorite.visibility = View.GONE
                    track.clear()
                    track.addAll(it.track)
                    favoriteAdapter?.notifyDataSetChanged()
                    favoriteAdapter?.updateData(it.track)
                }
                FavoriteState.Empty -> {
                    binding.placeHolderFavorite.visibility = View.VISIBLE
                    binding.rvFavoriteList.visibility = View.GONE
                }
            }
        }

        viewModel.getFavoriteTrack()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTrack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SAVE_TRACK = "track"
        fun newInstance() = FavoriteTracksFragment()
    }



}