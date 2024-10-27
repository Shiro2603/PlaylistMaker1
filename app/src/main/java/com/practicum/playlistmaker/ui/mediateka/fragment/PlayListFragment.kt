package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.ui.mediateka.view_model.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    private var _binding : FragmentPlaylistBinding? = null
    private val binding : FragmentPlaylistBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    private val viewModel by viewModel<PlayListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        fun newInstance() = PlayListFragment()
    }

}