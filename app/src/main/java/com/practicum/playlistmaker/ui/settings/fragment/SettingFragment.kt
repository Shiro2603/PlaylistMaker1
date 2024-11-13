package com.practicum.playlistmaker.ui.settings.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.databinding.FragmentSettingBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SettingFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>() { parametersOf(this) }
    private var _binding : FragmentSettingBinding? = null
    private val binding : FragmentSettingBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShare.setOnClickListener{
            viewModel.shareApp()
        }

        binding.buttonSupport.setOnClickListener{
            viewModel.openSupport()
        }

        binding.buttonUserAgreement.setOnClickListener{
            viewModel.openTerms()
        }


        viewModel.isDarkThemeEnabled.observe(viewLifecycleOwner) { isDarkThemeEnabled ->
            binding.switchTheme.isChecked = isDarkThemeEnabled
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleTheme(isChecked)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}