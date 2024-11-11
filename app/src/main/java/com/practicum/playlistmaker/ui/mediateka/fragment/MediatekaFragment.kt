package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediatekaBinding
import com.practicum.playlistmaker.ui.PagerAdapter


class MediatekaFragment : Fragment() {

    private var _binding : FragmentMediatekaBinding? = null
    private val binding : FragmentMediatekaBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    private lateinit var tabMediator : TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMediatekaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = PagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {
                tab, position ->
            when(position) {
                0 -> tab.text = this.getString(R.string.favoriteTracks)
                1 -> tab.text = this.getString(R.string.playList)
            }
        }

        tabMediator.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}