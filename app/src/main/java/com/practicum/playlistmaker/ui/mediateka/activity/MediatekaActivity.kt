package com.practicum.playlistmaker.ui.mediateka.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediatekaBinding
import com.practicum.playlistmaker.ui.PagerAdapter

class MediatekaActivity : AppCompatActivity() {

    private var _binding : ActivityMediatekaBinding? = null
    private val binding : ActivityMediatekaBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    private lateinit var tabMediator : TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnArrowBack.setOnClickListener { finish() }

        binding.viewPager.adapter = PagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, position ->
            when(position) {
                0 -> tab.text = this.getString(R.string.favoriteTracks)
                1 -> tab.text = this.getString(R.string.playList)
            }
        }

        tabMediator.attach()

    }


    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}