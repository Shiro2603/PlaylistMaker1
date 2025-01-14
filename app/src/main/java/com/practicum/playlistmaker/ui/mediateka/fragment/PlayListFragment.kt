package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.ui.mediateka.PlayListState
import com.practicum.playlistmaker.ui.mediateka.view_model.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    private var _binding : FragmentPlaylistBinding? = null
    private val binding : FragmentPlaylistBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    private val viewModel by viewModel<PlayListViewModel>()
    private var playListAdapter : PlayListAdapter? = null
    private val playList = ArrayList<PlayList>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNewPlayList.setOnClickListener {
            findNavController().navigate(R.id.newPlayListFragment)
        }

        playListAdapter = PlayListAdapter(playList)
        binding.rvPlayList.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvPlayList.adapter = playListAdapter


        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is PlayListState.Content -> {
                    binding.emptyPlayList.visibility = View.GONE
                    binding.rvPlayList.visibility = View.VISIBLE
                    playList.clear()
                    playList.addAll(it.playList)
                    playListAdapter?.notifyDataSetChanged()
                    playListAdapter?.updateData(it.playList)
                } else -> {
                    binding.emptyPlayList.visibility = View.VISIBLE
                binding.rvPlayList.visibility = View.GONE
                }

            }

        }

        viewModel.getPlayList()

    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlayList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = PlayListFragment()
    }

}