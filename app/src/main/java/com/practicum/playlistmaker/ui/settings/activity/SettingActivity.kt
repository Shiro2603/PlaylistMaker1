package com.practicum.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingActivity : AppCompatActivity() {
    private val viewModel by viewModel<SettingsViewModel>() { parametersOf(this) }
    private var _binding : ActivitySettingsBinding? = null
    private val binding : ActivitySettingsBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonArrowBack.setOnClickListener{
            finish()
        }

        binding.buttonShare.setOnClickListener{
            viewModel.shareApp()
        }

        binding.buttonSupport.setOnClickListener{
            viewModel.openSupport()
        }

        binding.buttonUserAgreement.setOnClickListener{
            viewModel.openTerms()
        }


        viewModel.isDarkThemeEnabled.observe(this) { isDarkThemeEnabled ->
            binding.switchTheme.isChecked = isDarkThemeEnabled
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleTheme(isChecked)
        }
    }
}