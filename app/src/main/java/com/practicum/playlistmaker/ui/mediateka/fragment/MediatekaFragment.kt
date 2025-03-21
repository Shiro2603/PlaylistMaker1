package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediatekaBinding
import com.practicum.playlistmaker.ui.mediateka.MediatekaScreen
import com.practicum.playlistmaker.ui.mediateka.PagerAdapter
import com.practicum.playlistmaker.ui.search.SearchScreen
import com.practicum.playlistmaker.ui.theme.AppTheme


class MediatekaFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    MediatekaScreen(navController)
                }
            }
        }
    }



}