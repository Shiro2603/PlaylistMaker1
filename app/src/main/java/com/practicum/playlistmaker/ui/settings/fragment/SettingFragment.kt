package com.practicum.playlistmaker.ui.settings.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.practicum.playlistmaker.databinding.FragmentSettingBinding
import com.practicum.playlistmaker.ui.settings.SettingScreen
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.practicum.playlistmaker.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SettingFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>() { parametersOf(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    SettingScreen(viewModel)
                }
            }
        }
    }


}