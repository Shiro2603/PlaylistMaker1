package com.practicum.playlistmaker.ui.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.ui.search.SearchScreen
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val searchViewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    SearchScreen(searchViewModel, navController)
                }
            }
        }
    }
}