package com.practicum.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.domain.settings.ThemeInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel.Companion.getViewModelFactory

class SettingActivity : AppCompatActivity() {

    private val themeInteractor : ThemeInteractor
        get() = Creator.provideThemeInteractor(applicationContext)

    private val sharingInteractor : SharingInteractor
        get() = Creator.provideSharingInteractor(this)

    private lateinit var viewModel : SettingsViewModel
    private lateinit var binding : ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, getViewModelFactory(themeInteractor, sharingInteractor))[SettingsViewModel::class.java]


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