package com.practicum.playlistmaker.ui.search.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.search.SearchScreen
import com.practicum.playlistmaker.ui.search.SearchScreenState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.SettingScreen
import com.practicum.playlistmaker.ui.theme.AppTheme
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val searchViewModel by viewModel<SearchViewModel>()
    private var isClickAllowed = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            debounce<Boolean>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                isClickAllowed = true
            }
        }
        return current
    }

    private fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        const val VALUE_KEY = "SearchText"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}