package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.ui.mediateka.MediatekaScreen
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